package com.mussayev.sudoku.data.repository

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider

interface AuthRepository {
    fun signInWithGoogle(account: GoogleSignInAccount): Task<AuthResult>
    fun signOut()
    fun getCurrentUser(): FirebaseUser?
}

class AuthRepositoryImpl(
    private val firebaseAuth: FirebaseAuth,
    private val googleSignInClient: GoogleSignInClient
) : AuthRepository {
    override fun signInWithGoogle(account: GoogleSignInAccount): Task<AuthResult> {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        return firebaseAuth.signInWithCredential(credential)
    }

    override fun signOut() {
        firebaseAuth.signOut()
        googleSignInClient.signOut()
    }

    override fun getCurrentUser(): FirebaseUser? {
        return firebaseAuth.currentUser
    }
}
