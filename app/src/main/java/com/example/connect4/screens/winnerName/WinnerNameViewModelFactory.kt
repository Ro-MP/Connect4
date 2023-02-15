package com.example.connect4.screens.winnerName

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.connect4.database.ScoreDatabaseDao

class WinnerNameViewModelFactory (
    private val winner: Long,
    private val dataSource: ScoreDatabaseDao,
    private val application: Application) : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        // Check if there is a ScoreViewModel class available
        if (modelClass.isAssignableFrom(WinnerNameViewModel::class.java)) {
            return WinnerNameViewModel(winner, dataSource, application) as T
        }
        throw IllegalAccessException("Unknown ViewModel class")
    }
}