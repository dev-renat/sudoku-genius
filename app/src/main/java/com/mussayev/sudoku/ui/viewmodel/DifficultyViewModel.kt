package com.mussayev.sudoku.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mussayev.sudoku.data.model.Difficulty
import com.mussayev.sudoku.data.repository.DifficultyRepository

class DifficultyViewModel(private val difficultyRepository: DifficultyRepository): ViewModel() {
    val difficultyLiveData = MutableLiveData<String>()

    fun init() {
        difficultyLiveData.value = difficultyRepository.getCurrentDifficultyName()
    }

    fun getCurrentDifficulty(): Difficulty {
        return difficultyRepository.getCurrentDifficulty()
    }

    fun getCurrentDifficultyName(): String {
        return difficultyRepository.getCurrentDifficultyName()
    }

    fun incrementDifficulty() {
        difficultyRepository.incrementDifficulty()
        difficultyLiveData.value = getCurrentDifficultyName()
    }

    fun decrementDifficulty() {
        difficultyRepository.decrementDifficulty()
        difficultyLiveData.value = getCurrentDifficultyName()
    }
}