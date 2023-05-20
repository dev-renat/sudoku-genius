package com.mussayev.sudoku.ui.activity.main

import androidx.lifecycle.ViewModel
import com.mussayev.sudoku.data.remote.FirebaseRemote
import com.mussayev.sudoku.data.repository.SettingsRepository
import com.mussayev.sudoku.data.repository.StorageGameRepository

class MainViewModel(
    private val storageGameRepository: StorageGameRepository,
    private val firebaseRemote: FirebaseRemote,
    private val settingsRepository: SettingsRepository,
    ) : ViewModel() {

//    fun onInternetConnectionChanged(connected: Boolean) {
//        if (connected) {
//            val localUserScore = storageGameRepository.loadUserScore()
//
//            // Если у нас есть локальные данные, сохраняем их в Firebase
//            if (localUserScore != null) {
//                firebaseRemote.saveUserScore(localUserScore)
//            }
//
//            // Загружаем данные пользователя из Firebase и сохраняем их локально
//            firebaseRemote.loadUserScore { userScore ->
//                if (userScore != null) {
//                    storageGameRepository.saveUserScore(userScore)
//                }
//            }
//        }
//    }

    fun incrementLaunches() {
        settingsRepository.incrementLaunches()
    }

    fun setNumLaunches(num: Long) {
        settingsRepository.setNumLaunches(num)
    }

    fun getNumLaunches(): Long {
        return settingsRepository.getNumLaunches()
    }
}