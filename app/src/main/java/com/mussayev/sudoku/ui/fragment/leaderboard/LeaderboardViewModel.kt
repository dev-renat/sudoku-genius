package com.mussayev.sudoku.ui.fragment.leaderboard

import androidx.lifecycle.ViewModel
import com.google.firebase.database.Query
import com.mussayev.sudoku.data.remote.FirebaseRemote

class LeaderboardViewModel(
    private val firebaseRemote: FirebaseRemote
) : ViewModel() {

    fun getLeaderboard(): Query {
        return firebaseRemote.getLeaderboard()
    }
}