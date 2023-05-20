package com.mussayev.sudoku.ui.fragment.guide

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.appbar.MaterialToolbar
import com.mussayev.sudoku.R
import com.mussayev.sudoku.databinding.FragmentGuideBinding

class GuideFragment : Fragment() {
    private lateinit var binding: FragmentGuideBinding
    private lateinit var navController: NavController
    private lateinit var toolbar: MaterialToolbar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentGuideBinding.inflate(inflater)
        navController = findNavController()

        val view = binding.root

        view.alpha = 0f
        view.animate().alpha(1f).setDuration(500).start()

        toolbar = binding.toolbar
        toolbar.title = getString(R.string.guide)
        toolbar.setNavigationOnClickListener {
            // Обработка события нажатия на кнопку "назад"
            navController.navigateUp()
        }

        return view
    }

    override fun onDestroyView() {
        view?.animate()?.alpha(0f)?.setDuration(500)?.start()
        super.onDestroyView()
    }
}