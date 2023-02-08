package com.example.connect4.screens.score

import android.app.Application
import android.provider.SyncStateContract.Helpers.insert
import android.text.format.DateUtils
import android.text.method.TextKeyListener.clear
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.connect4.database.Score
import com.example.connect4.database.ScoreDatabaseDao
import kotlinx.coroutines.launch

class ScoreViewModel(
    val winner: String,
    val timeLength: String,
    val database: ScoreDatabaseDao,
    application: Application) : AndroidViewModel(application) {


    private var score = MutableLiveData<Score?>()
    private val scores = database.getAllScores()
    val scoresString = Transformations.map(scores) { scores ->
        var text = ""
        scores.forEach {
            text = "$text${it.date}               ${it.winner}                         ${it.timeLength}\n"
        }
        return@map text
    }

    init {
        onSaveScore()
        initializeScore()
    }

    private fun initializeScore() {
        viewModelScope.launch {
            score.value = getScoreFromDatabase()
        }
    }

    private suspend fun getScoreFromDatabase(): Score? {
        return database.getLastScore()
    }

    fun onSaveScore() {
        viewModelScope.launch {
            val newScore = Score()
            newScore.winner = winner
            newScore.timeLength = timeLength
            insert(newScore)
        }
    }

    private suspend fun insert(score: Score) {
        database.insert(score)
    }

    fun onClear(){
        viewModelScope.launch {
            clear()
            score.value = null
        }
    }

    private suspend fun clear() {
        database.clear()
    }

}