package com.example.connect4.screens.scoreDetail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.connect4.database.ScoreDatabaseDao


class ScoreDetailViewModelFactory (
    private val scoreId: Long,
    private val dataSource: ScoreDatabaseDao) : ViewModelProvider.Factory {

        @Suppress("unchecked_cast")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            // Check if there is a ScoreViewModel class available
            if (modelClass.isAssignableFrom(ScoreDetailViewModel::class.java)) {
                Log.i("ScoreViewModel", "ScoreViewModel Created - Factory")
                return ScoreDetailViewModel(scoreId, dataSource) as T
            }
            throw IllegalAccessException("Unknown ViewModel class")
        }
}