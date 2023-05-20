package com.mussayev.sudoku.data.repository

import com.google.gson.Gson
import com.mussayev.sudoku.data.model.Difficulty
import com.mussayev.sudoku.data.model.SavedGame
import com.mussayev.sudoku.data.model.Statistics
import com.mussayev.sudoku.data.remote.FirebaseRemote

class StorageGameRepository(
    private val difficultyRepository: DifficultyRepository,
    private val preferencesManager: PreferencesManager,
    ) {

    fun saveGame(savedGame: SavedGame) {
        val gameDataJson = Gson().toJson(savedGame)
        preferencesManager.saveGameData(difficultyRepository.getCurrentDifficultyName(), gameDataJson)
    }

    // Загрузка игровых данных
    fun loadGame(): SavedGame? {
        val gameDataJson = preferencesManager.loadGameData(difficultyRepository.getCurrentDifficultyName())
        return if (gameDataJson != null) {
            Gson().fromJson(gameDataJson, SavedGame::class.java)
        } else {
            null
        }
    }

    // Очистить игровые данные
    fun clearGameData() {
        preferencesManager.clearGameData(difficultyRepository.getCurrentDifficultyName())
    }

//    fun saveUserScore(gameData: UserScore) {
//        val gameDataJson = Gson().toJson(gameData)
//        preferencesManager.saveUserScore(gameDataJson)
//    }
//
//    fun loadUserScore(): UserScore? {
//        val gameDataJson = preferencesManager.loadUserScore()
//        return if (gameDataJson != null) {
//            Gson().fromJson(gameDataJson, UserScore::class.java)
//        } else {
//            null
//        }
//    }

    //
    fun saveStatistics(gameData: Statistics) {
        val difficulty = difficultyRepository.getCurrentDifficultyName()
        val statisticsJson = Gson().toJson((gameData))
        preferencesManager.saveStatistics(difficulty, statisticsJson)
    }

    fun loadStatistics(difficulty: String) : Statistics {
        val statisticsJson = preferencesManager.loadStatistics(difficulty)
        return if (statisticsJson != null) {
            Gson().fromJson(statisticsJson, Statistics::class.java)
        } else {
            Statistics(0,0,0,0, 0, 0)
        }
    }
}
