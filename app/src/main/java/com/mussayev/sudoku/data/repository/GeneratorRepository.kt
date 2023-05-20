package com.mussayev.sudoku.data.repository

import com.mussayev.sudoku.data.model.Difficulty
import kotlin.random.Random

class GeneratorRepository {

    private lateinit var fullBoard: Array<IntArray>

    fun generate(difficulty: Difficulty): Array<IntArray> {
        val board = Array(9) { IntArray(9) }
        generateFullBoard(board)
        fullBoard = cloneBoard(board)
        removeCells(board, getCellsToRemove(difficulty))

        return board
    }

    private fun cloneBoard(board: Array<IntArray>): Array<IntArray> {
        val newBoard = Array(9) { IntArray(9) }
        for (i in 0..8) {
            for (j in 0..8) {
                newBoard[i][j] = board[i][j]
            }
        }
        return newBoard
    }

    private fun getCellsToRemove(difficulty: Difficulty) = when (difficulty) {
        Difficulty.BEGINNER -> 15
        Difficulty.EASY -> 30
        Difficulty.MEDIUM -> 40
        Difficulty.HARD -> 50
        Difficulty.EXTREME -> 54
    }

    private fun generateFullBoard(board: Array<IntArray>): Boolean {
        for (i in 0..8) {
            for (j in 0..8) {
                if (board[i][j] == 0) {
                    val availableNumbers = (1..9).toMutableList()

                    while (availableNumbers.isNotEmpty()) {
                        val randomIndex = Random.nextInt(availableNumbers.size)
                        val number = availableNumbers[randomIndex]
                        availableNumbers.removeAt(randomIndex)

                        if (isValidMove(board, i, j, number)) {
                            board[i][j] = number
                            if (generateFullBoard(board)) {
                                return true
                            }
                            board[i][j] = 0
                        }
                    }
                    return false
                }
            }
        }
        return true
    }

    private fun isValidMove(board: Array<IntArray>, row: Int, col: Int, number: Int): Boolean {
        for (i in 0..8) {
            if (board[row][i] == number || board[i][col] == number) {
                return false
            }
        }

        val startRow = row / 3 * 3
        val startCol = col / 3 * 3

        for (i in 0..2) {
            for (j in 0..2) {
                if (board[startRow + i][startCol + j] == number) {
                    return false
                }
            }
        }

        return true
    }

    private fun removeCells(board: Array<IntArray>, count: Int) {
        repeat(count) {
            val row = Random.nextInt(9)
            val col = Random.nextInt(9)

            if (board[row][col] != 0) {
                board[row][col] = 0
            } else {
                removeCells(board, 1)
            }
        }
    }

    fun getFullBoard(): Array<IntArray> {
        return fullBoard
    }

    fun getCellNumber(board: Array<IntArray>, row: Int, col: Int): Int? {
        if (board[row][col] != 0) {
            return null
            //throw IllegalArgumentException("Hint is requested for a filled cell")
        }
        return fullBoard[row][col]
    }
}