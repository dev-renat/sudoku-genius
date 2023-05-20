package com.mussayev.sudoku.ui.fragment.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mussayev.sudoku.data.model.Difficulty
import com.mussayev.sudoku.data.repository.GameRepository
import com.mussayev.sudoku.data.repository.DifficultyRepository
import com.mussayev.sudoku.data.repository.StorageGameRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel(
    private val difficultyRepository: DifficultyRepository,
    private val storageGameRepository: StorageGameRepository
) : ViewModel() {

    val hasSavedGameLiveData = MutableLiveData(false)

//    fun getCurrentDifficultyName(): String {
//        return difficultyRepository.getCurrentDifficultyName()
//    }
//
//    fun getCurrentDifficulty(): Difficulty {
//        return difficultyRepository.getCurrentDifficulty()
//    }
//
//    fun incrementDifficulty() {
//        difficultyRepository.incrementDifficulty()
//        difficultyLiveData.value = getCurrentDifficultyName()
//    }
//
//    fun decrementDifficulty() {
//        difficultyRepository.decrementDifficulty()
//        difficultyLiveData.value = getCurrentDifficultyName()
//    }

    fun loadGame() {
        if (storageGameRepository.loadGame() != null) {
            hasSavedGameLiveData.postValue(true)
        } else {
            hasSavedGameLiveData.postValue(false)
        }
    }
}