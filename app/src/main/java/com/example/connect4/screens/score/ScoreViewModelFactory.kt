package com.example.connect4.screens.score

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


// this class will be responsible for instantiating
class ScoreViewModelFactory(private val winner: String) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ScoreViewModel::class.java)) {
            return ScoreViewModel(winner) as T
        }
        throw IllegalAccessException("Unknown ViewModel class")
    }

}

