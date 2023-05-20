package com.mussayev.sudoku.data.repository

import com.mussayev.sudoku.data.model.Difficulty

class DifficultyRepository(private val preferencesManager: PreferencesManager) {

    fun getCurrentDifficultyName(): String {
        return preferencesManager.getDifficulty() ?: Difficulty.BEGINNER.name
    }

    fun getCurrentDifficulty() : Difficulty {
        return if (preferencesManager.getDifficulty() == Difficulty.BEGINNER.name) {
            Difficulty.BEGINNER
        } else if (preferencesManager.getDifficulty() == Difficulty.EASY.name) {
            Difficulty.EASY
        } else if (preferencesManager.getDifficulty() == Difficulty.MEDIUM.name) {
            Difficulty.MEDIUM
        } else if (preferencesManager.getDifficulty() == Difficulty.HARD.name) {
            Difficulty.HARD
        } else if (preferencesManager.getDifficulty() == Difficulty.EXTREME.name) {
            Difficulty.EXTREME
        }  else {
            Difficulty.BEGINNER
        }
    }

    fun incrementDifficulty() {

        when (preferencesManager.getDifficulty()) {
            Difficulty.BEGINNER.name -> {
                preferencesManager.saveDifficulty(Difficulty.EASY.name)
            }
            Difficulty.EASY.name -> {
                preferencesManager.saveDifficulty(Difficulty.MEDIUM.name)
            }
            Difficulty.MEDIUM.name -> {
                preferencesManager.saveDifficulty(Difficulty.HARD.name)
            }
            Difficulty.HARD.name -> {
                preferencesManager.saveDifficulty(Difficulty.EXTREME.name)
            }
            else -> {
                preferencesManager.saveDifficulty(Difficulty.BEGINNER.name)
            }
        }
    }

    fun decrementDifficulty() {
        when (preferencesManager.getDifficulty()) {
            Difficulty.EXTREME.name -> {
                preferencesManager.saveDifficulty(Difficulty.HARD.name)
            }
            Difficulty.HARD.name -> {
                preferencesManager.saveDifficulty(Difficulty.MEDIUM.name)
            }
            Difficulty.MEDIUM.name -> {
                preferencesManager.saveDifficulty(Difficulty.EASY.name)
            }
            Difficulty.EASY.name -> {
                preferencesManager.saveDifficulty(Difficulty.BEGINNER.name)
            }
            else -> {
                preferencesManager.saveDifficulty(Difficulty.EXTREME.name)
            }
        }
    }
}