package com.mussayev.sudoku.ui.fragment.settings

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.appbar.MaterialToolbar
import com.mussayev.sudoku.data.manager.SoundManager
import com.mussayev.sudoku.databinding.FragmentSettingsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : Fragment() {
    private lateinit var binding: FragmentSettingsBinding
    private lateinit var navController: NavController
    private lateinit var toolbar: MaterialToolbar
    private val viewModel: SettingsViewModel by viewModel()

    override fun onDestroyView() {
        view?.animate()?.alpha(0f)?.setDuration(500)?.start()
        super.onDestroyView()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater)
        navController = findNavController()
        val view = binding.root

        view.alpha = 0f
        view.animate().alpha(1f).setDuration(500).start()

        toolbar = binding.toolbar
        toolbar.setNavigationOnClickListener {
            // Обработка события нажатия на кнопку "назад"
            navController.navigateUp()
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        init()
    }

    private fun init() {
        binding.soundSwitch.isChecked = viewModel.isSoundEnabled()
        binding.soundSwitch.setOnCheckedChangeListener { _, isChecked ->
            viewModel.setSound(isChecked)
            if (isChecked) {
                viewModel.playSound(SoundManager.SoundEffect.EFFECT_SPLAT)
            }
        }

        binding.musicSwitch.isChecked = viewModel.isMusicEnabled()
        binding.musicSwitch.setOnCheckedChangeListener { _, isChecked ->
            viewModel.setMusic(isChecked)
            if (isChecked) {
                viewModel.playSound(SoundManager.SoundEffect.EFFECT_SPLAT)
            }
        }

        //
        binding.vibrationSwitch.isChecked = viewModel.isVibrationEnabled()
        binding.vibrationSwitch.setOnCheckedChangeListener { _, isChecked ->
            viewModel.setVibration(isChecked)
            if (viewModel.isVibrationEnabled()) {
                viewModel.vibration()
                if (isChecked) {
                    viewModel.playSound(SoundManager.SoundEffect.EFFECT_SPLAT)
                }
            }
        }

        //
        binding.errorLimitSwitch.isChecked = viewModel.isErrorLimitEnabled()
        binding.errorLimitSwitch.setOnCheckedChangeListener { _, isChecked ->
            viewModel.setErrorLimit(isChecked)
            if (isChecked) {
                viewModel.playSound(SoundManager.SoundEffect.EFFECT_SPLAT)
            }
        }

        //
        binding.goToGooglePlay.setOnClickListener {
            try {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=${context?.packageName}")))
            } catch (e: ActivityNotFoundException) {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=${context?.packageName}")))
            }
        }
    }
}