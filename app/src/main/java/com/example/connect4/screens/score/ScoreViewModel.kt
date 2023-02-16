package com.example.connect4.screens.score

import android.app.Application
import android.provider.SyncStateContract.Helpers.insert
import android.text.format.DateUtils
import android.text.method.TextKeyListener.clear
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.connect4.database.Score
import com.example.connect4.database.ScoreDatabaseDao
import kotlinx.coroutines.launch

class ScoreViewModel(
    var scoreId: Long?,
    val winner: String?,
    val timeLength: String?,
    val database: ScoreDatabaseDao,
    application: Application) : AndroidViewModel(application) {

//    private val _currentWinner = MutableLiveData<String?>()
//    val currentWinner : LiveData<String?>
//        get() = _currentWinner


    private val _navigateToScoreDetail = MutableLiveData<Long?>()
    val navigateToScoreDetail: LiveData<Long?>
        get() = _navigateToScoreDetail

    private var currentTimeLength : String? = ""

    private var score = MutableLiveData<Score?>()
    val scores = database.getAllScores()

    val scoresString = Transformations.map(scores) { scores ->
        var text = ""
        scores.forEach {
            //text = "$text${it.date}               ${it.winner}                         ${it.timeLength}\n"
            text = "$text${it.date}      ${it.winner}        ${it.timeLength}         ${it.scoreId}\n"
        }
        return@map text
    }

    val currentWinner = Transformations.map(scores) {
        return@map it[0].winner
    }
    val currentId = Transformations.map(scores) {
        return@map it[0].scoreId
    }

    init {
        initializeScore()
    }

    private fun initializeScore() {
        if (scoreId == null){
            Log.i("ScoreViewModel", "Score Id: Was null")
            val newScore = Score()
            Log.i("ScoreViewModel", "newScore Id: ${newScore.scoreId}")
            newScore.winner = winner?:""
            newScore.timeLength = timeLength?:"0:0"
            onSaveScore(newScore)
            //onGetLastScoreFromDatabase()
            Log.i("ScoreViewModel", "Score Id: Now is ${score.value?.scoreId?:"Nulisimo"}")

        } else {
            Log.i("ScoreViewModel", "Score Id: $scoreId")
            onGetScoreFromDabase(scoreId as Long)
        }
        //_currentWinner.value = "${score.value?.winner} id:${score.value?.scoreId}"
        //currentTimeLength = score.value?.timeLength

    }

    fun getNewScoreId(): Long?{
        return score.value?.scoreId
    }


    /*
        Start - Database
     */

    private fun onGetLastScoreFromDatabase()  {
        viewModelScope.launch{
            score.value = getLastScoreFromDatabase()
        }
    }

    private suspend fun getLastScoreFromDatabase(): Score? {
        return database.getLastScore()
    }

    private fun onGetScoreFromDabase(scoreId: Long) {
        viewModelScope.launch{
            score.value = getScoreFromDabase(scoreId)
        }
    }
    private suspend fun getScoreFromDabase(scoreId: Long): Score?{
        return database.get(scoreId)
    }


    fun onSaveScore(score: Score) {
        viewModelScope.launch {
            insert(score)
            onGetLastScoreFromDatabase()
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

    /*
        End - Database
     */


    fun onScoreClicked(id: Long) {
        _navigateToScoreDetail.value = id
    }

    fun onScoreDetailNavigated() {
        _navigateToScoreDetail.value = null
    }


}