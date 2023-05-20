package com.mussayev.sudoku.ui.fragment.endgame

import androidx.lifecycle.ViewModel
import com.mussayev.sudoku.data.model.Cell
import com.mussayev.sudoku.data.model.SavedGame
import com.mussayev.sudoku.data.repository.StorageGameRepository
import com.mussayev.sudoku.utils.StringUtils

class EndgameViewModel(private val storageGameRepository: StorageGameRepository) : ViewModel() {

    var difficulty: String = ""
    var elapsedTime: Long = 0
    var errors: Int = 0
    // Список ячеек
    var cells = mutableListOf<Cell>()

    // Вспомогательный класс для работы со строками
    private val stringUtils = StringUtils()

    /* Метод загрузки сохраненной игры */
    fun loadGame(): Boolean {
        val savedGame = storageGameRepository.loadGame() ?: return false
        restoreGameState(savedGame)
        return true
    }

    // Метод для восстановления состояния игры из сохраненных данных
    private fun restoreGameState(savedGame: SavedGame) {
        cells = stringUtils.stringToBoardCells(savedGame.cells)
        //board = stringUtils.stringToBoard(savedGame.board)
        difficulty = savedGame.difficulty
        elapsedTime = savedGame.elapsedTime
        errors = savedGame.errors
        //hintsLiveData.value = savedGame.hints
        //errorsLiveData.value = savedGame.errors

        //
        storageGameRepository.clearGameData()
    }
}