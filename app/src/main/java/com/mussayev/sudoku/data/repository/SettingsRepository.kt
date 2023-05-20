package com.mussayev.sudoku.data.repository

class SettingsRepository(private val preferencesManager: PreferencesManager) {

    fun saveTheme(value: String) {
        preferencesManager.saveTheme(value)
    }

    fun currentThemeName(): String? {
        return preferencesManager.getTheme()
    }

    fun setSound(sound: Boolean) {
        preferencesManager.isSoundEnabled = sound
    }

    fun isSoundEnabled(): Boolean {
        return preferencesManager.isSoundEnabled
    }

    fun setMusic(music: Boolean) {
        preferencesManager.isMusicEnabled = music
    }

    fun isMusicEnabled(): Boolean {
        return preferencesManager.isMusicEnabled
    }

    //vibration
    fun setVibration(vibration: Boolean) {
        preferencesManager.isVibrationEnabled = vibration
    }

    fun isVibrationEnabled(): Boolean {
        return preferencesManager.isVibrationEnabled
    }

    //vibration
    fun setErrorLimit(errorLimit: Boolean) {
        preferencesManager.isErrorLimitEnabled = errorLimit
    }

    fun isErrorLimitEnabled(): Boolean {
        return preferencesManager.isErrorLimitEnabled
    }

    fun incrementLaunches() {
        preferencesManager.incrementLaunches()
    }

    fun setNumLaunches(num: Long) {
        return preferencesManager.setNumLaunches(num)
    }

    fun getNumLaunches(): Long {
        return preferencesManager.getNumLaunches()
    }
}