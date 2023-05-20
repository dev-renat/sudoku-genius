package com.mussayev.sudoku.ui.fragment.game

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Chronometer
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.mussayev.sudoku.R
import com.mussayev.sudoku.data.manager.SoundManager
import com.mussayev.sudoku.data.model.Difficulty
import com.mussayev.sudoku.databinding.FragmentGameBinding
import com.mussayev.sudoku.domain.manager.AdManager
import com.mussayev.sudoku.ui.activity.main.MainActivity
import com.mussayev.sudoku.ui.custom.SudokuBoardView
import com.mussayev.sudoku.ui.dialog.*
import com.mussayev.sudoku.utils.DifficultyUtils
import com.mussayev.sudoku.utils.UIUtils
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class GameFragment : Fragment(),
    SudokuBoardView.OnTouchListener,
    GameOverDialogListener,
    ResetGameDialogListener,
    EndgameDialogListener,
    RestoreHintDialogListener,
    ConfirmExitDialogListener
{
    private lateinit var binding: FragmentGameBinding
    private lateinit var navController: NavController
    private var isLoadGame: Boolean = false
    private val viewModel: GameViewModel by viewModel()
    private val adManager: AdManager by inject()
    private lateinit var chronometer: Chronometer
    private lateinit var numberButtons: List<Button>

    private lateinit var uiUtils: UIUtils

    private var isDialogShowing = false
    private var isEndgame = false

    private lateinit var numberButtonImages: Array<Drawable?>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentGameBinding.inflate(inflater)
        navController = findNavController()

        isLoadGame = arguments?.getBoolean("load_game") ?: false

        val view = binding.root

        view.alpha = 0f
        view.animate().alpha(1f).setDuration(500).start()

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {

                if (!isDialogShowing) {
                    isDialogShowing = true

                    val dialog = ConfirmExitDialog(
                        getString(R.string.exit),
                        getString(R.string.confirm_exit_game)
                    )
                    dialog.isCancelable = false
                    dialog.setListener(this@GameFragment)
                    dialog.show(requireActivity().supportFragmentManager, "dialog_confirm_exit")
                }
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        init()
    }

    override fun onDestroyView() {
        view?.animate()?.alpha(0f)?.setDuration(500)?.start()

        viewModel.stopAllSounds()
        viewModel.stopMusic()
        viewModel.musicRelease()

        super.onDestroyView()
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadMusic(R.raw.background)
        resumeGame()

        adManager.loadInterstitialAd({}, { _ -> })
    }

    override fun onPause() {
        super.onPause()
        pauseGame()
    }

    private fun init() {
        val typedArray = context?.resources?.obtainTypedArray(R.array.cell_images)
        typedArray?.let {
            numberButtonImages = Array(typedArray.length()) { i ->
                typedArray.getDrawable(i)
            }
            typedArray.recycle()
        }

        uiUtils = (activity as MainActivity).uiUtils

        viewModel.resetGame()

        chronometer = binding.cMeter

        startGame()

        binding.difficultyTextView.text = DifficultyUtils().getName(requireActivity(), viewModel.getCurrentDifficultyName())

        //
        viewModel.errorsLiveData().observe(viewLifecycleOwner) {errors ->
            viewModel.setErrors(errors)

            val limit = viewModel.getErrorsLimit()

            if (viewModel.isErrorsLimitEnabled()) {
                binding.errorsTextView.text = getString(R.string.mistakes_limit, errors, limit)

                if (errors >= limit) {
                    isEndgame = true
                    val timeInSeconds = (getElapsedTime() / 1000)
                    viewModel.saveStatistics(timeInSeconds, false)
                    if (!isDialogShowing) {
                        isDialogShowing = true
                        chronometer.stop()
                        viewModel.stopMusic()
                        viewModel.playSound(SoundManager.SoundEffect.EFFECT_GAME_OVER)
                        val dialog = GameOverDialog()
                        dialog.isCancelable = false
                        dialog.setListener(this)
                        dialog.show(requireActivity().supportFragmentManager, "dialog_game_over")
                    }
                }
            } else {
                binding.errorsTextView.text = getString(R.string.mistakes, errors)
            }
        }

        numberButtons = listOf(
            binding.oneButton,
            binding.twoButton,
            binding.threeButton,
            binding.fourButton,
            binding.fiveButton,
            binding.sixButton,
            binding.sevenButton,
            binding.eightButton,
            binding.nineButton
        )

        numberButtons.forEachIndexed { index, button ->
            button.setTextAppearance(R.style.buttonInputText)
            button.setOnClickListener {
                viewModel.handleInput(index + 1)
            }
        }

        val draftButton = binding.draftButton
        draftButton.setBackgroundResource(R.drawable.button_circle)
        draftButton.setImageResource(R.drawable.icon_draft)
        draftButton.setOnClickListener {
            viewModel.changeNoteTakingState()
        }

        binding.clearButton.setOnClickListener {
            viewModel.clearCell()
        }

        viewModel.boardLiveData().observe(viewLifecycleOwner) { cells ->
            binding.sudokuBoardView.updateCells(cells)
            val timeInSeconds = (getElapsedTime() / 1000)

            if (viewModel.isSolved()) {
                val statistics = viewModel.saveStatistics(timeInSeconds, true)
                //val statistics = viewModel.getStatistics(viewModel.getCurrentDifficultyName())
                if (!isDialogShowing) {
                    chronometer.stop()
                    viewModel.stopMusic()
                    viewModel.playSound(SoundManager.SoundEffect.EFFECT_WIN)
                    isDialogShowing = true

                    viewModel.clearGameData()
                    isEndgame = true

                    val dialog = EndgameDialog(timeInSeconds, statistics.bestTime)
                    dialog.isCancelable = false
                    dialog.setListener(this)
                    dialog.show(requireActivity().supportFragmentManager, "dialog_endgame")
                }
            }
        }

        viewModel.selectedCellLiveData().observe(viewLifecycleOwner) {
            updateSelectedCellUI(it)
        }

        viewModel.getIsTakingNotesLiveData().observe(viewLifecycleOwner) {
            updateNoteTakingUI(it)
        }

        viewModel.getHighlightedKeysLiveData().observe(viewLifecycleOwner) {
            updateHighlightedKeys(it)
        }

        binding.sudokuBoardView.registerListener(this)

        viewModel.hintsLiveData().observe(viewLifecycleOwner) {
            binding.hintsTextView.text = it.toString()//if (it < 1) "+" else it.toString()
            viewModel.setIsHint(it != 0)
        }

        binding.hintTextView.setOnClickListener {
            if (viewModel.getIsHint()) {
                viewModel.hint()
            } else {
                //Toast.makeText(requireActivity(), getString(R.string.used_all_hints), Toast.LENGTH_SHORT).show()

//                adManager.showInterstitialAd(requireActivity()) {
//                    val adShown = adManager.wasAdShown()
//                    if (adShown) {
//                        // Реклама была показана
//                        viewModel.incrementHint()
//                        viewModel.hint()
//                    } else {
//                        // Реклама не была показана
//                        viewModel.incrementHint()
//                        viewModel.hint()
//                    }
//                }
//                isDialogShowing = true
//                val dialog = RestoreHintDialog()
//                dialog.isCancelable = false
//                dialog.setListener(this)
//                dialog.show(requireActivity().supportFragmentManager, "dialog_restore_hint")
            }
        }

        binding.resetButton.setOnClickListener {
            if (!isDialogShowing) {
                isDialogShowing = true
                val dialog = ResetGameDialog()
                dialog.isCancelable = false
                dialog.setListener(this)
                dialog.show(requireActivity().supportFragmentManager, "dialog_reset_game")
            }
        }
    }

    private fun resumeGame() {
        chronometer.format = "00:00:00"
        chronometer.setOnChronometerTickListener {
            val elapsedMillis = getElapsedTime()
            val hours = (elapsedMillis / 3600000).toInt()
            val minutes = ((elapsedMillis - hours * 3600000) / 60000).toInt()
            val seconds = ((elapsedMillis - hours * 3600000 - minutes * 60000) / 1000).toInt()
            chronometer.text = String.format("%02d:%02d:%02d", hours, minutes, seconds)
        }
        chronometer.base = SystemClock.elapsedRealtime() - viewModel.getElapsedTime()
        chronometer.start()
        viewModel.playMusic()
    }

    private fun startGame() {
        if (isLoadGame) {
            viewModel.loadGame()
        } else {
            viewModel.newGame()
        }
    }

    private fun pauseGame() {
        chronometer.stop()
        val elapsedTime = getElapsedTime()
        viewModel.setElapsedTime(elapsedTime)
        if (!isEndgame) {
            viewModel.saveGame()
        }
        viewModel.pauseMusic()
    }

    private fun getElapsedTime(): Long {
        return SystemClock.elapsedRealtime() - chronometer.base
    }

    private fun updateSelectedCellUI(cell: Pair<Int, Int>?) = cell?.let {
        binding.sudokuBoardView.updateSelectedCellUI(cell.first, cell.second)
    }

    private fun updateNoteTakingUI(isNoteTaking: Boolean?) {
        isNoteTaking?.let {
            binding.sudokuBoardView.toggleNoteMode(it)
            if (it) {
                viewModel.vibration()
                viewModel.playSound(SoundManager.SoundEffect.EFFECT_DRAFT)
                binding.draftButton.setImageResource(R.drawable.icon_draft_active)
                binding.draftButton.setBackgroundResource(R.drawable.button_circle_active)
            } else {
                binding.draftButton.setImageResource(R.drawable.icon_draft)
                binding.draftButton.setBackgroundResource(R.drawable.button_circle)
            }
        }
    }

    private fun updateHighlightedKeys(set: Set<Int>?) = set?.let {
        numberButtons.forEachIndexed { index, button ->
            if (set.contains(index + 1)) {
                button.setTextAppearance(R.style.buttonInputTextActive)
                button.setBackgroundResource(R.drawable.button_input_active)
            } else {
                button.setTextAppearance(R.style.buttonInputText)
                button.setBackgroundResource(R.drawable.button_input)
            }
        }
    }

    private fun exitWithAds() {
        navController.popBackStack()
//        adManager.showInterstitialAd(requireActivity()) {
//            val adShown = adManager.wasAdShown()
//            if (adShown) {
//                // Реклама была показана
//                navController.popBackStack()
//            } else {
//                // Реклама не была показана
//                navController.popBackStack()
//            }
//        }
    }

    override fun onCellTouched(row: Int, col: Int) {
        if (!isEndgame) {
            viewModel.updateSelectedCell(row, col)
        }
    }

    override fun gameOverDialogOnClickCancel() {
        viewModel.clearGameData()
        exitWithAds()
        isDialogShowing = false
    }

    override fun resetGameDialogOnClickOk() {
        chronometer.stop()
        viewModel.stopMusic()
        viewModel.resetGame()
        viewModel.newGame()
        isDialogShowing = false
        isEndgame = false
        chronometer.base = SystemClock.elapsedRealtime()
        chronometer.start()
        viewModel.loadMusic(R.raw.background)
        viewModel.playMusic()
    }

    override fun resetGameDialogOnClickCancel() {
        isDialogShowing = false
    }

    override fun restoreHintDialogOnClickOk() {
        adManager.showInterstitialAd(requireActivity()) {
            val adShown = adManager.wasAdShown()
            if (adShown) {
                // Реклама была показана
                viewModel.incrementHint()
            } else {
                // Реклама не была показана
                Toast.makeText(requireActivity(), getString(R.string.ad_unavailable_try_later), Toast.LENGTH_SHORT).show()
            }
        }
        isDialogShowing = false
    }

    override fun restoreHintDialogOnClickCancel() {
        isDialogShowing = false
    }

    override fun endgameDialogOnClickCancel() {
        exitWithAds()
        isDialogShowing = false
    }

    override fun confirmExitDialogOnClickOk() {
        isDialogShowing = false
        navController.popBackStack()
    }

    override fun confirmExitDialogOnClickCancel() {
        isDialogShowing = false
    }
}