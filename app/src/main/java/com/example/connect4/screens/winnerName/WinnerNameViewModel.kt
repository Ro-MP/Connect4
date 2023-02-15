package com.example.connect4.screens.winnerName

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.connect4.database.Score
import com.example.connect4.database.ScoreDatabaseDao
import kotlinx.coroutines.launch

class WinnerNameViewModel(
    val scoreId: Long,
    val dataSource: ScoreDatabaseDao,
    application: Application) : AndroidViewModel(application) {

    private val _currentScore = MutableLiveData<Score>()
    val currentScore : LiveData<Score>
        get() = _currentScore

    private val _newName = MutableLiveData<String>()
    val newName: LiveData<String>
        get() = _newName

    private val _navigateToScoreFragment = MutableLiveData<Boolean>()
    val navigateToScoreFragment: LiveData<Boolean>
        get() = _navigateToScoreFragment

    private val _isEditTextNull = MutableLiveData<Boolean>()
    val isEditTextNull: LiveData<Boolean>
        get() = _isEditTextNull

    private val _onBackToScoreFragment = MutableLiveData<Boolean>()
    val onBackToScoreFragment: LiveData<Boolean>
        get() = _onBackToScoreFragment


    init {
        getCurrentScore()
    }

    private fun getCurrentScore() {
        viewModelScope.launch {
            val score = getScore(scoreId)
            score?.apply {
                _currentScore.value = this
            }
        }
    }

    private suspend fun getScore(scoreId: Long): Score? {
        return dataSource.get(scoreId)
    }


    fun onDone(){
        if (isEditTextNull.value == false){
            newName.value?.apply {
                _currentScore.value?.winner = this
            }
            _currentScore.value?.apply {
                onUpdateScore(this)
            }
            _onBackToScoreFragment.value = true
        }
    }

    fun onCancel(){
        _onBackToScoreFragment.value = true
    }

    private fun onUpdateScore(score: Score){
        viewModelScope.launch {
            updateScore(score)
        }
    }

    private suspend fun updateScore(score: Score){
        dataSource.update(score)
    }


    fun setNewName(newName: String) {
        _newName.value = newName
        _isEditTextNull.value = (newName == "" || newName == null)

    }




}



