package com.mussayev.sudoku.data.repository

//import com.google.android.gms.auth.api.signin.GoogleSignInAccount
//import com.google.android.gms.auth.api.signin.GoogleSignInClient
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.auth.FirebaseUser
//import com.google.firebase.auth.GoogleAuthProvider
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.tasks.await
//import kotlinx.coroutines.withContext
//
//class UserRepository(
//    private val firebaseAuth: FirebaseAuth,
//    val googleSignInClient: GoogleSignInClient
//) {
//
//    suspend fun firebaseAuthWithGoogle(account: GoogleSignInAccount): FirebaseUser? {
//        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
//        return withContext(Dispatchers.IO) {
//            firebaseAuth.signInWithCredential(credential).await().user
//        }
//    }
//}
