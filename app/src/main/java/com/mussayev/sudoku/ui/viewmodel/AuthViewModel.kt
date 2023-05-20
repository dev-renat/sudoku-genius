package com.mussayev.sudoku.ui.viewmodel

import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseUser
import com.mussayev.sudoku.data.repository.AuthRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class AuthViewModel(private val authRepository: AuthRepository, private val googleSignInClient: GoogleSignInClient) : ViewModel() {
    private val _authenticationState = MutableLiveData<AuthenticationState>()
    val authenticationState: LiveData<AuthenticationState>
        get() = _authenticationState

    init {
        val currentUser = authRepository.getCurrentUser()
        _authenticationState.value =
            if (currentUser != null) AuthenticationState.Authenticated
            else AuthenticationState.Unauthenticated
    }

    fun getGoogleSignInIntent(): Intent {
        return googleSignInClient.signInIntent
    }

    fun signInWithGoogle(account: GoogleSignInAccount) {
        viewModelScope.launch {
            try {
                authRepository.signInWithGoogle(account).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        _authenticationState.value = AuthenticationState.Authenticated
                    } else {
                        _authenticationState.value = AuthenticationState.Failed(task.exception ?: Exception("Unknown error"))
                    }
                }
            } catch (e: Exception) {
                _authenticationState.value = AuthenticationState.Failed(e)
            }
        }
    }

    fun signOut() {
        authRepository.signOut()
        _authenticationState.value = AuthenticationState.Unauthenticated
    }

    fun getCurrentUser(): FirebaseUser? {
        return authRepository.getCurrentUser()
    }
}

sealed class AuthenticationState {
    object Authenticated : AuthenticationState()
    object Unauthenticated : AuthenticationState()
    data class Failed(val error: Throwable) : AuthenticationState()
}