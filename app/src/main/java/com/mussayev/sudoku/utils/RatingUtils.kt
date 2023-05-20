package com.mussayev.sudoku.utils

import kotlin.math.max

class RatingUtils {
    companion object {
        fun calculate(timeInSeconds: Int, errorCount: Int): Int {
            val timePenalty = 1 // Наказание за каждую секунду
            val errorPenalty = 10 // Наказание за каждую ошибку
            val initialScore = 7200 // Начальный балл

            val score = initialScore - (timeInSeconds * timePenalty) - (errorCount * errorPenalty)
            return max(score, 0)
        }
    }
}