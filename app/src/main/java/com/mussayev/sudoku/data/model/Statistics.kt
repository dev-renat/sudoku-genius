package com.mussayev.sudoku.data.model

class Statistics(
    var totalGames: Int,
    var totalTime: Long,
    var bestTime: Long,
    var lastGameTime: Long,
    var loss: Int,
    var win: Int,
)