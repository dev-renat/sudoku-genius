package com.mussayev.sudoku.data.model

data class SavedGame(
    val cells: String,
    val board: String,
    val fullBoard: String,
    val selectedRow: Int,
    val selectedCol: Int,
    var difficulty: String,
    var elapsedTime: Long = 0,
    var errors: Int = 0,
    var errorsLimitEnabled: Boolean,
    var hints: Int = 2,
)