package com.mussayev.sudoku.ui.fragment.game

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.mussayev.sudoku.data.manager.MusicManager
import com.mussayev.sudoku.data.manager.SoundManager
import com.mussayev.sudoku.data.manager.VibrationManager
import com.mussayev.sudoku.data.model.Cell
import com.mussayev.sudoku.data.model.Difficulty
import com.mussayev.sudoku.data.model.Statistics
import com.mussayev.sudoku.data.model.UserScore
import com.mussayev.sudoku.data.remote.FirebaseRemote
import com.mussayev.sudoku.data.repository.DifficultyRepository
import com.mussayev.sudoku.data.repository.GameRepository
import com.mussayev.sudoku.data.repository.StorageGameRepository
import com.mussayev.sudoku.utils.RatingUtils
import kotlin.math.min

class GameViewModel(
    private val gameRepository: GameRepository,
    private val firebaseRemote: FirebaseRemote,
    private val storageGameRepository: StorageGameRepository,
    private val difficultyRepository: DifficultyRepository,
    private val soundManager: SoundManager,
    private val musicManager: MusicManager,
    private val vibrationManager: VibrationManager
) : ViewModel() {

    private var isHint = true
    private var errors = 0
    private val auth = Firebase.auth

    fun loadGame() {
        if (!gameRepository.loadGame()) {
            gameRepository.newGame()
        }
    }

    fun newGame() {
        gameRepository.newGame()
    }

    fun resetGame() {
        gameRepository.resetGame()
    }

    fun updateSelectedCell(row: Int, col: Int) {
        gameRepository.updateSelectedCell(row, col)
    }

    fun boardLiveData(): MutableLiveData<List<Cell>> {
        return gameRepository.boardLiveData
    }

    fun selectedCellLiveData(): MutableLiveData<Pair<Int, Int>> {
        return gameRepository.selectedCellLiveData
    }

    //
    fun getIsTakingNotesLiveData(): MutableLiveData<Boolean> {
        return gameRepository.isTakingNotesLiveData
    }

    fun getHighlightedKeysLiveData(): MutableLiveData<Set<Int>> {
        return gameRepository.highlightedKeysLiveData
    }

    //
    fun changeNoteTakingState() {
        gameRepository.changeNoteTakingState()
    }

    fun handleInput(number: Int) {
        gameRepository.handleInput(number)
    }

    fun hintsLiveData(): MutableLiveData<Int> {
        return gameRepository.hintsLiveData
    }

    fun errorsLiveData(): MutableLiveData<Int> {
        return gameRepository.errorsLiveData
    }

    fun hint() {
        gameRepository.hint()
    }

    fun incrementHint() {
        gameRepository.incrementHint()
    }

    fun getIsHint() : Boolean {
        return isHint
    }

    fun setIsHint(bool: Boolean) {
        isHint = bool
    }

    fun clearCell() {
        gameRepository.clearCell()
    }

    fun setElapsedTime(value: Long) {
        gameRepository.setElapsedTime(value)
    }

    fun getElapsedTime(): Long {
        return gameRepository.getElapsedTime()
    }

    fun decrementError() {
        gameRepository.decrementError()
    }

    //
    fun getErrorsLimit() : Int {
        return gameRepository.getErrorsLimit()
    }

    fun saveGame() {
        gameRepository.saveGame()
    }

    fun clearGameData() {
        gameRepository.clearGameData()
    }

    //
    fun getStatistics(difficulty: String): Statistics {
        return storageGameRepository.loadStatistics(difficulty)
    }

    //
    fun isSolved(): Boolean {
        return gameRepository.isSolved()
    }

    fun saveStatistics(currentTime: Long, endGameResult: Boolean): Statistics {
        val statistics = getStatistics(getCurrentDifficultyName())

        if (statistics.bestTime == 0L) {
            statistics.bestTime = currentTime
        } else if (statistics.bestTime > currentTime) {
            statistics.bestTime = currentTime
        }
        statistics.apply {
            totalGames += 1
            totalTime += currentTime
            lastGameTime = currentTime
            if (endGameResult) win += 1
            else loss += 1
        }
        storageGameRepository.saveStatistics(statistics)
        return statistics
    }

    //
    fun getCurrentDifficultyName(): String {
        return difficultyRepository.getCurrentDifficultyName()
    }

    //
    fun playSound(soundEffect: SoundManager.SoundEffect) {
        soundManager.playSound(soundEffect)
    }

    fun stopAllSounds() {
        soundManager.stopAllSounds()
    }

    fun playMusic() {
        musicManager.playMusic()
    }

    fun pauseMusic() {
        musicManager.pauseMusic()
    }

    fun loadMusic(resourceInt: Int) {
        musicManager.loadMusic(resourceInt)
    }

    fun stopMusic() {
        musicManager.stopMusic()
    }

    fun musicRelease() {
        musicManager.release()
    }

    //
    fun vibration() {
        vibrationManager.vibrate(10)
    }

    //
    fun isErrorsLimitEnabled(): Boolean {
        return gameRepository.isErrorsLimitEnabled()
    }

    fun getErrors() : Int {
        return errors
    }

    fun setErrors(value: Int) {
        errors = value
    }
}