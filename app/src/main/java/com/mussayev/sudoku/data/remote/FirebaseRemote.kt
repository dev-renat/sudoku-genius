package com.mussayev.sudoku.data.remote

import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase

class FirebaseRemote {
    private val auth = Firebase.auth
    private val database = FirebaseDatabase.getInstance()

    private val userScores = "user_scores"

/*    fun saveUserScore(userScore: UserScore) {
        auth.currentUser?.let { user ->
            database.reference.child(userScores).child(user.uid).setValue(userScore)
        }
    }

    fun loadUserScore(onComplete: (UserScore?) -> Unit) {
        auth.currentUser?.let { user ->
            database.reference.child(userScores).child(user.uid)
                .addListenerForSingleValueEvent(object :
                    ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val userScore = snapshot.getValue(UserScore::class.java)
                        onComplete(userScore)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        onComplete(null)
                    }
                })
        }
    }*/

    //
    fun getLeaderboard(): Query {
        val usersRef = database.getReference(userScores)
        val topUsersQuery = usersRef.orderByChild("rating")
        return topUsersQuery.limitToLast(20)
    }
}