package com.mussayev.sudoku.ui.fragment.account

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.mussayev.sudoku.data.model.Statistics
import com.mussayev.sudoku.data.repository.AuthRepository
import com.mussayev.sudoku.data.repository.StorageGameRepository

class AccountViewModel(
    private val storageGameRepository: StorageGameRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val auth = Firebase.auth

    val totalGamesLiveData = MutableLiveData<String>()

    fun logout() {
        authRepository.signOut()
    }

    fun getUser(): FirebaseUser? {
        if (auth.currentUser?.isAnonymous == true) return null
        return auth.currentUser
    }

    fun getStatistics(difficulty: String): Statistics {
        return storageGameRepository.loadStatistics(difficulty)
    }
}