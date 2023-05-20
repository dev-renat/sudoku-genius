package com.mussayev.sudoku.utils

import android.content.Context
import com.mussayev.sudoku.R
import com.mussayev.sudoku.data.model.Difficulty

class DifficultyUtils() {
    fun getName(context: Context, level: String) : String {
        return when (level) {
            Difficulty.BEGINNER.name -> context.getString(R.string.beginner)
            Difficulty.EASY.name -> context.getString(R.string.easy)
            Difficulty.MEDIUM.name -> context.getString(R.string.medium)
            Difficulty.HARD.name -> context.getString(R.string.hard)
            else -> context.getString(R.string.extreme)
        }
    }

    fun getById(id: Int) : Difficulty {
        return when (id) {
            1 -> Difficulty.BEGINNER
            2 -> Difficulty.EASY
            3 -> Difficulty.MEDIUM
            4 -> Difficulty.HARD
            5 -> Difficulty.EXTREME
            else -> Difficulty.BEGINNER
        }
    }

    fun nextDifficultyName(name: String): String {
        return when(name) {
            Difficulty.BEGINNER.name -> Difficulty.EASY.name
            Difficulty.EASY.name -> Difficulty.MEDIUM.name
            Difficulty.MEDIUM.name -> Difficulty.HARD.name
            Difficulty.HARD.name -> Difficulty.EXTREME.name
            Difficulty.EXTREME.name -> Difficulty.BEGINNER.name
            else -> Difficulty.BEGINNER.name
        }
    }

    fun prevDifficultyName(name: String): String {
        return when(name) {
            Difficulty.EXTREME.name -> Difficulty.HARD.name
            Difficulty.HARD.name -> Difficulty.MEDIUM.name
            Difficulty.MEDIUM.name -> Difficulty.EASY.name
            Difficulty.EASY.name -> Difficulty.BEGINNER.name
            Difficulty.BEGINNER.name -> Difficulty.EXTREME.name
            else -> Difficulty.EXTREME.name
        }
    }
}