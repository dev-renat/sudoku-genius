package com.mussayev.sudoku.data.manager

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import com.mussayev.sudoku.R
import com.mussayev.sudoku.data.repository.SettingsRepository

class SoundManager(private val context: Context, private val settingsRepository: SettingsRepository) {

    // Другие звуковые эффекты
    enum class SoundEffect {
        EFFECT_SUCCESS,
        EFFECT_ERROR,
        EFFECT_HINT,
        EFFECT_DRAFT,
        EFFECT_SPLAT,
        EFFECT_WIN,
        EFFECT_GAME_OVER,
    }

    private lateinit var soundPool: SoundPool
    private val soundMap: HashMap<SoundEffect, Int> = HashMap()

    init {
    }

    fun load() {
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        soundPool = SoundPool.Builder()
            .setMaxStreams(5)
            .setAudioAttributes(audioAttributes)
            .build()

        // Звуковые файлы
        soundMap[SoundEffect.EFFECT_SUCCESS] = soundPool.load(context, R.raw.success1, 1)
        soundMap[SoundEffect.EFFECT_ERROR] = soundPool.load(context, R.raw.error, 1)
        soundMap[SoundEffect.EFFECT_HINT] = soundPool.load(context, R.raw.hint, 1)
        soundMap[SoundEffect.EFFECT_DRAFT] = soundPool.load(context, R.raw.splat, 1)
        soundMap[SoundEffect.EFFECT_SPLAT] = soundPool.load(context, R.raw.splat, 1)
        soundMap[SoundEffect.EFFECT_WIN] = soundPool.load(context, R.raw.win2, 1)
        soundMap[SoundEffect.EFFECT_GAME_OVER] = soundPool.load(context, R.raw.gameover, 1)
    }

    fun playSound(soundEffect: SoundEffect) {
        soundMap[soundEffect]?.let {
            if (settingsRepository.isSoundEnabled()) {
                soundPool.play(it, 1f, 1f, 1, 0, 1f)
            }
        }
    }

    fun release() {
        soundPool.stop(-1)
        soundPool.release()
    }

    fun stopAllSounds() {
        soundPool.stop(-1)
    }
}
