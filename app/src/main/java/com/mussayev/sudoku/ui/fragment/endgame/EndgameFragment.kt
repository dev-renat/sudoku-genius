package com.mussayev.sudoku.ui.fragment.endgame

import android.os.Bundle
import android.os.SystemClock
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Chronometer
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.mussayev.sudoku.R
import com.mussayev.sudoku.databinding.FragmentEndgameBinding
import com.mussayev.sudoku.utils.DifficultyUtils
import org.koin.androidx.viewmodel.ext.android.viewModel


class EndgameFragment : Fragment() {
    private lateinit var binding: FragmentEndgameBinding
    private lateinit var navController: NavController

    private val viewModel: EndgameViewModel by viewModel()
    private lateinit var chronometer: Chronometer

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentEndgameBinding.inflate(inflater)

        val view = binding.root

        view.alpha = 0f
        view.animate().alpha(1f).setDuration(500).start()

        return view
    }

    override fun onDestroyView() {
        view?.animate()?.alpha(0f)?.setDuration(500)?.start()
        super.onDestroyView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        init()
    }

    private fun init() {
        navController = findNavController()

        chronometer = binding.cMeter

        if (viewModel.loadGame()) {
            chronometer.base = SystemClock.elapsedRealtime() - viewModel.elapsedTime
            binding.difficulty.text = DifficultyUtils().getName(requireActivity(), viewModel.difficulty)
            //binding.sudokuBoardView.updateCells(viewModel.cells)

            binding.newGameButton.setOnClickListener {
                val navOption = NavOptions.Builder().setPopUpTo(R.id.endgameFragment, true).build()
                navController.navigate(R.id.action_endgameFragment_to_gameFragment, null, navOption)
            }

            binding.homeButton.setOnClickListener {
                val navOption = NavOptions.Builder().setPopUpTo(R.id.homeFragment, true).build()
                navController.navigate(R.id.action_endgameFragment_to_homeFragment, null, navOption)
            }
        }
    }
}