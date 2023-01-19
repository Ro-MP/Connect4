package com.example.connect4.screens.score

import android.util.Log
import androidx.lifecycle.ViewModel

class ScoreViewModel(winner: String) : ViewModel() {

    var winner = winner

    init {
        Log.i("ScoreViewModel", "The winner is $winner")
    }

}