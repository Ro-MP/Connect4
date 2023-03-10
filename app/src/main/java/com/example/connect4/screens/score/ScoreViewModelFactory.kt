package com.example.connect4.screens.score

import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.connect4.database.ScoreDatabaseDao


// this class will be responsible for instantiating
class ScoreViewModelFactory(
    private val scoreId: Long?,
    private val winner: String?,
    private val timeLength: String?,
    private val dataSource: ScoreDatabaseDao,
    private val application: Application) : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        // Check if there is a ScoreViewModel class available
        if (modelClass.isAssignableFrom(ScoreViewModel::class.java)) {
            Log.i("ScoreViewModel", "ScoreViewModel Created - Factory")
            return ScoreViewModel(scoreId, winner, timeLength, dataSource, application) as T
        }
        throw IllegalAccessException("Unknown ViewModel class")
    }

}

