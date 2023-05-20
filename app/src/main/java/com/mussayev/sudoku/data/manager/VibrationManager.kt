package com.mussayev.sudoku.data.manager

import android.content.Context
import android.os.VibrationEffect
import android.os.Vibrator
import com.mussayev.sudoku.data.repository.SettingsRepository

class VibrationManager(private val context: Context, private val settingsRepository: SettingsRepository) {

    private val vibrator = context.getSystemService(Vibrator::class.java)

    fun vibrate(duration: Long) {
        if (settingsRepository.isVibrationEnabled()) {
            vibrator?.vibrate(
                VibrationEffect.createOneShot(
                    duration,
                    VibrationEffect.DEFAULT_AMPLITUDE
                )
            )
        }
    }

    fun cancel() {
        vibrator?.cancel()
    }
}

