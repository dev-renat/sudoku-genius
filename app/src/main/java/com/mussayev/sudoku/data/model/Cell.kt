package com.mussayev.sudoku.data.model

data class Cell(
    val row: Int,
    val col: Int,
    var value: Int,
    var isStarting: Boolean = false,
    var isSuccess: Boolean? = null,
    var isHint: Boolean = false,
    var notes: MutableSet<Int> = mutableSetOf()
)