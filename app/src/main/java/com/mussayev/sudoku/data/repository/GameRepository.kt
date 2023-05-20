package com.mussayev.sudoku.data.repository

import androidx.lifecycle.MutableLiveData
import com.mussayev.sudoku.data.manager.SoundManager
import com.mussayev.sudoku.data.manager.VibrationManager
import com.mussayev.sudoku.data.model.Cell
import com.mussayev.sudoku.data.model.Difficulty
import com.mussayev.sudoku.data.model.SavedGame
import com.mussayev.sudoku.data.remote.FirebaseRemote
import com.mussayev.sudoku.utils.RatingUtils
import com.mussayev.sudoku.utils.StringUtils
import kotlin.math.min

class GameRepository(
    private val generatorRepository: GeneratorRepository,
    private val storageGameRepository: StorageGameRepository,
    private val difficultyRepository: DifficultyRepository,
    private val soundManager: SoundManager,
    private val vibrationManager: VibrationManager,
    private val settingsRepository: SettingsRepository,
    ) {

    // LiveData для хранения состояния ячеек доски
    var boardLiveData = MutableLiveData<List<Cell>>()
    // LiveData для хранения выбранной ячейки
    var selectedCellLiveData = MutableLiveData<Pair<Int, Int>>()

    // LiveData для состояния добавления заметок
    var isTakingNotesLiveData = MutableLiveData<Boolean>()
    // LiveData для хранения подсвеченных клавиш
    var highlightedKeysLiveData = MutableLiveData<Set<Int>>()

    // LiveData для количества подсказок
    var hintsLiveData = MutableLiveData<Int>()

    // LiveData для количества ошибок
    var errorsLiveData = MutableLiveData<Int>()

    // Основная доска
    private lateinit var board: Array<IntArray>
    // Полная доска с правильными значениями
    private lateinit var fullBoard: Array<IntArray>
    // Список ячеек
    private var cells = mutableListOf<Cell>()

    // Время, проведенное в игре
    private var elapsedTime: Long = 0
    // Выбранная строка
    private var selectedRow = -1
    // Выбранная колонка
    private var selectedCol = -1

    // Состояние добавления заметок
    private var isTakingNotes = false

    // Лимит ошибок
    private val errorsLimit = 5
    // Флаг лимита ошибок
    private var errorsLimitEnabled = true

    // Вспомогательный класс для работы со строками
    private val stringUtils = StringUtils()

    // Метод начала игры
    private fun startGame() {
        boardLiveData.postValue(cells)
        selectedCellLiveData.postValue(Pair(selectedRow, selectedCol))
        isTakingNotesLiveData.postValue(isTakingNotes)
    }

    // Метод для создания новой игры
    fun newGame() {
        board = generatorRepository.generate(difficultyRepository.getCurrentDifficulty())
        fullBoard = generatorRepository.getFullBoard()
        hintsLiveData.value = 2
        errorsLimitEnabled = settingsRepository.isErrorLimitEnabled()

        var cellIndex = 0
        for (i in 0 until 9) {
            for (j in 0 until 9) {
                val cell = Cell(
                    row = i,
                    col = j,
                    value = board[i][j],
                    isStarting = board[i][j] > 0,
                )
                cells.add(cellIndex, cell)
                cellIndex++
            }
        }
        startGame()
    }

    /* Метод загрузки сохраненной игры */
    fun loadGame(): Boolean {
        val savedGame = storageGameRepository.loadGame() ?: return false
        restoreGameState(savedGame)
        startGame()
        return true
    }

    // Метод для восстановления состояния игры из сохраненных данных
    private fun restoreGameState(savedGame: SavedGame) {
        cells = stringUtils.stringToBoardCells(savedGame.cells)
        board = stringUtils.stringToBoard(savedGame.board)
        fullBoard = stringUtils.stringToBoard(savedGame.fullBoard)
        selectedRow = savedGame.selectedRow
        selectedCol = savedGame.selectedCol
        elapsedTime = savedGame.elapsedTime
        hintsLiveData.value = savedGame.hints
        errorsLiveData.value = savedGame.errors
        errorsLimitEnabled = savedGame.errorsLimitEnabled
    }

    // Метод для установки времени, проведенного в игре
    fun setElapsedTime(value: Long) {
        elapsedTime = value
    }

    // Метод для получения времени, проведенного в игре
    fun getElapsedTime(): Long {
        return elapsedTime
    }

    // Метод для сохранения текущей игры
    fun saveGame() {
        val savedGame = SavedGame(
            cells = stringUtils.boardCellsToString(cells),
            board = stringUtils.boardToString(board),
            fullBoard = stringUtils.boardToString(fullBoard),
            selectedRow = selectedRow,
            selectedCol = selectedCol,
            difficulty = difficultyRepository.getCurrentDifficultyName(),
            elapsedTime = elapsedTime,
            hints = hintsLiveData.value ?: 0,
            errors = errorsLiveData.value ?: 0,
            errorsLimitEnabled = errorsLimitEnabled
        )

        storageGameRepository.saveGame(savedGame)
    }

    // Очистить игровые данные
    fun clearGameData() {
        storageGameRepository.clearGameData()
    }

    // Метод для обновления выбранной ячейки
    fun updateSelectedCell(row: Int, col: Int) {
        if (row == -1 || col == -1) return

        val cell = getCell(row, col)
        cell.let {
            if (!it.isStarting && it.isSuccess != true) {
                selectedRow = row
                selectedCol = col
                selectedCellLiveData.postValue(Pair(row, col))

                if (isTakingNotes) {
                    highlightedKeysLiveData.postValue(cell.notes)
                }
            }
        }
    }

    // Метод для переключения режима добавления заметок
    fun changeNoteTakingState() {
        if (selectedRow == -1 || selectedCol == -1) return

        val cell = getCell(selectedRow, selectedCol)

        if (cell.isSuccess == false) {
            cell.isSuccess = null
            cell.value = 0
        }

        if (cell.value > 0) return

        isTakingNotes = !isTakingNotes

        isTakingNotesLiveData.postValue(isTakingNotes)

        val curNotes = if (isTakingNotes) {
            getCell(selectedRow, selectedCol).notes
        } else {
            setOf()
        }

        highlightedKeysLiveData.postValue(curNotes)
    }

    // Метод для обработки пользовательского ввода
    fun handleInput(number: Int) {
        if (selectedRow == -1 || selectedCol == -1) return
        errorsLiveData.value?.let { if (it >= errorsLimit && errorsLimitEnabled) { return } }

        val cell = getCell(selectedRow, selectedCol)

        if (cell.value > 0 && cell.value == number && cell.isSuccess == false) {
            cell.value = 0
            cell.isSuccess = null
            boardLiveData.postValue(cells)
            return
        }

        cell.let {
            if (cell.isStarting || cell.isSuccess == true) return

            if (isTakingNotes && cell.value < 1) {
                if (cell.notes.contains(number)) {
                    cell.notes.remove(number)
                } else {
                    cell.notes.add(number)
                }
                highlightedKeysLiveData.postValue(cell.notes)
            } else {
                if (number == fullBoard[selectedRow][selectedCol]) {
                    cell.isSuccess = true
                    cell.notes.clear()
                    soundManager.playSound(SoundManager.SoundEffect.EFFECT_SUCCESS)
                } else {
                    cell.isSuccess = false
                    incrementError()
                    soundManager.playSound(SoundManager.SoundEffect.EFFECT_ERROR)
                    vibrationManager.vibrate(50)
                }

                cell.value = number
            }
            boardLiveData.postValue(cells)
        }
    }

    // Метод для использования подсказки
    fun hint() {
        if (selectedRow == -1 || selectedCol == -1) return
        errorsLiveData.value?.let { if (it >= errorsLimit && errorsLimitEnabled) { return } }

        val cell = getCell(selectedRow, selectedCol)

        cell.let {
            if (it.isStarting || it.isSuccess == true) return

            hintsLiveData.value?.let { hintAmount ->
                if (hintAmount > 0) {
                    hintsLiveData.value = hintAmount - 1

                    it.isSuccess = true
                    it.isHint = true
                    it.notes.clear()
                    it.value = fullBoard[selectedRow][selectedCol]

                    boardLiveData.postValue(cells)
                    soundManager.playSound(SoundManager.SoundEffect.EFFECT_HINT)
                } else {
                    soundManager.playSound(SoundManager.SoundEffect.EFFECT_ERROR)
                }
            }

            resetNotes()
        }
    }

    //
    fun incrementHint() {
        hintsLiveData.value?.let {
            hintsLiveData.value = it + 1
        }
    }

    // Метод для очистки ячейки или заметок
    fun clearCell() {
        if (selectedRow == -1 || selectedCol == -1) return
        val cell = getCell(selectedRow, selectedCol)
        if (cell.isStarting || cell.isSuccess == true) return

        if (isTakingNotes) {
            if (cell.value < 1) {
                cell.notes.clear()
                highlightedKeysLiveData.postValue(setOf())
            }
        } else {
            cell.value = 0
            cell.isSuccess = null
        }

        boardLiveData.postValue(cells)
    }

    // Метод для сброса состояния заметок
    private fun resetNotes() {
        isTakingNotes = false
        isTakingNotesLiveData.postValue(false)
        highlightedKeysLiveData.postValue(emptySet())
    }

    // Метод для сброса игры
    fun resetGame() {
        // Сброс базовых значений
        elapsedTime = 0
        selectedRow = -1
        selectedCol = -1
        errorsLimitEnabled = settingsRepository.isErrorLimitEnabled()

        // Сброс заметок и режима заметок
        resetNotes()

        // Сброс LiveData значений
        boardLiveData.value = emptyList()
        selectedCellLiveData.value = Pair(-1, -1)
        isTakingNotesLiveData.value = false
        highlightedKeysLiveData.value = emptySet()
        hintsLiveData.value = 0
        errorsLiveData.value = 0

        // Сброс массивов доски
        board = Array(9) { IntArray(9) }
        fullBoard = Array(9) { IntArray(9) }
        cells.clear()
    }

    //
    private fun incrementError() {
        errorsLiveData.value?.let {
            errorsLiveData.value = it + 1
        }
    }

    fun decrementError() {
        errorsLiveData.value?.let {
            errorsLiveData.value = it - 1
        }
    }

    // Метод для получения ячейки по указанным строке и столбцу
    private fun getCell(row: Int, col: Int): Cell {
        if (row !in 0..8 || col !in 0..8) {
            throw IllegalArgumentException("Row and column must be within 0..8. Given row: $row, col: $col")
        }
        return cells[row * 9 + col]
    }

    //
    fun isSolved(): Boolean {
        val successCellCount = cells.count { it.isSuccess == true || it.isStarting }
        return successCellCount == (9 * 9)
    }

    fun getErrorsLimit(): Int {
        return errorsLimit
    }

    fun isErrorsLimitEnabled(): Boolean {
        return errorsLimitEnabled
    }
}