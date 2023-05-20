package com.mussayev.sudoku.ui.fragment.account

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.appbar.MaterialToolbar
import com.google.firebase.auth.FirebaseUser
import com.mussayev.sudoku.R
import com.mussayev.sudoku.databinding.FragmentAccountBinding
import com.mussayev.sudoku.ui.viewmodel.DifficultyViewModel
import com.mussayev.sudoku.utils.DifficultyUtils
import com.mussayev.sudoku.utils.StringUtils
import org.koin.androidx.viewmodel.ext.android.viewModel

class AccountFragment : Fragment() {
    private lateinit var binding: FragmentAccountBinding
    private lateinit var navController: NavController
    private lateinit var toolbar: MaterialToolbar
    private val viewModel: AccountViewModel by viewModel()
    private val difficultyViewModel: DifficultyViewModel by viewModel()

    override fun onDestroyView() {
        view?.animate()?.alpha(0f)?.setDuration(500)?.start()
        super.onDestroyView()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentAccountBinding.inflate(inflater)
        navController = findNavController()

        val view = binding.root

        view.alpha = 0f
        view.animate().alpha(1f).setDuration(500).start()

        toolbar = binding.toolbar
        toolbar.title = getString(R.string.game_statistics)
        toolbar.setNavigationOnClickListener {
            navController.navigateUp()
        }
//        toolbar.setOnMenuItemClickListener { menuItem ->
//            when (menuItem.itemId) {
//                R.id.action_logout -> {
//                    viewModel.logout()
//                    val navOption = NavOptions.Builder().setPopUpTo(R.id.accountFragment, true).build()
//                    navController.navigate(R.id.action_accountFragment_to_homeFragment, null, navOption)
//                    true
//                }
//                else -> false
//            }
//        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        init()
    }

    private fun init() {
        difficultyViewModel.init()

        showStatistics()

        difficultyViewModel.difficultyLiveData.observe(viewLifecycleOwner) {difficulty ->
            binding.difficultyTextView.text = DifficultyUtils().getName(requireActivity(), difficulty)
        }

        binding.decrementDifficultyButton.setOnClickListener {
            difficultyViewModel.decrementDifficulty()
            val animation = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_right)
            binding.difficultyTextView.startAnimation(animation)
            showStatistics()
        }

        binding.incrementDifficultyButton.setOnClickListener {
            difficultyViewModel.incrementDifficulty()
            val animation = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_left)
            binding.difficultyTextView.startAnimation(animation)
            showStatistics()
        }
    }

    private fun showStatistics() {
        val statistics = viewModel.getStatistics(difficultyViewModel.getCurrentDifficultyName())
        binding.totalGamesTextView.text = statistics.totalGames.toString()

        val totalTime = statistics.totalTime
        val bestTime = statistics.bestTime
        val lastTime = statistics.lastGameTime
        val win = statistics.win
        val loss = statistics.loss

        viewModel.totalGamesLiveData.observe(viewLifecycleOwner) {count ->
            binding.totalGamesTextView.text = count
        }
        binding.bestTimeTextView.text = StringUtils.secondsToTime(bestTime)
        binding.totalTimeTextView.text = StringUtils.secondsToTime(totalTime)
        binding.lastTimeTextView.text = StringUtils.secondsToTime(lastTime)
        binding.winTextView.text = win.toString()
        binding.lossTextView.text = loss.toString()
    }
}