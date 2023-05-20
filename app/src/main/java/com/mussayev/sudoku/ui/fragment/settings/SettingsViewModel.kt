package com.mussayev.sudoku.ui.fragment.settings

import androidx.lifecycle.ViewModel
import com.mussayev.sudoku.data.manager.SoundManager
import com.mussayev.sudoku.data.manager.VibrationManager
import com.mussayev.sudoku.data.repository.SettingsRepository

class SettingsViewModel(
    private val settingsRepository: SettingsRepository,
    private val soundManager: SoundManager,
    private val vibrationManager: VibrationManager
) : ViewModel() {

    fun playSound(soundEffect: SoundManager.SoundEffect) {
        soundManager.playSound(soundEffect)
    }

    fun setSound(sound: Boolean) {
        settingsRepository.setSound(sound)
    }

    fun isSoundEnabled(): Boolean {
        return settingsRepository.isSoundEnabled()
    }

    fun setMusic(music: Boolean) {
        settingsRepository.setMusic(music)
    }

    fun isMusicEnabled(): Boolean {
        return settingsRepository.isMusicEnabled()
    }

    fun setVibration(vibration: Boolean) {
        settingsRepository.setVibration(vibration)
    }

    fun isVibrationEnabled(): Boolean {
        return settingsRepository.isVibrationEnabled()
    }

    //
    fun vibration() {
        vibrationManager.vibrate(50)
    }

    fun setErrorLimit(errorLimit: Boolean) {
        settingsRepository.setErrorLimit(errorLimit)
    }

    fun isErrorLimitEnabled(): Boolean {
        return settingsRepository.isErrorLimitEnabled()
    }
}