package com.mussayev.sudoku.data.model

data class UserScore(
    var username: String,
    var photo: String,
    var rating: Int
)
{
    constructor() : this("", "",0) // Для Firebase
}
