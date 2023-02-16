package com.example.connect4.screens.scoreDetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.connect4.database.Score
import com.example.connect4.database.ScoreDatabaseDao
import kotlinx.coroutines.launch

class ScoreDetailViewModel(val scoreId: Long, val database: ScoreDatabaseDao): ViewModel() {

    private val _score = MutableLiveData<Score>()
    val score : LiveData<Score>
        get() = _score

    init {
        onGetScoreFromDabase(scoreId)

    }


    override fun onCleared() {
        super.onCleared()
    }

    private fun onGetScoreFromDabase(scoreId: Long) {
        viewModelScope.launch{
            _score.value = getScoreFromDabase(scoreId)
        }
    }
    private suspend fun getScoreFromDabase(scoreId: Long): Score?{
        return database.get(scoreId)
    }
}