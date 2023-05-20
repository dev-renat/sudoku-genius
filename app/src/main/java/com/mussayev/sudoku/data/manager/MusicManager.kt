package com.mussayev.sudoku.data.manager

import android.content.Context
import android.media.MediaPlayer
import com.mussayev.sudoku.data.repository.SettingsRepository

class MusicManager(private val context: Context, private val settingsRepository: SettingsRepository) {

    private var mediaPlayer: MediaPlayer? = null

    fun playMusic(isLooping: Boolean = true) {
        if (settingsRepository.isMusicEnabled()) {
            mediaPlayer?.let {
                it.setVolume(0.04f, 0.04f)
                it.isLooping = isLooping
                it.start()
            }
        }
    }

    fun pauseMusic() {
        if (settingsRepository.isMusicEnabled()) {
            mediaPlayer?.pause()
        }
    }

    fun stopMusic() {
        mediaPlayer?.let { player ->
            if (player.isPlaying) {
                player.stop()
            }
            player.reset()
            player.release()
            mediaPlayer = null
        }
    }

    fun release() {
        mediaPlayer?.release()
        mediaPlayer = null
    }

    fun loadMusic(resourceId: Int) {
        if (settingsRepository.isMusicEnabled()) {
            mediaPlayer = MediaPlayer.create(context, resourceId)
        }
    }
}
