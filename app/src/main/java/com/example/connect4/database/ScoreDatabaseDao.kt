package com.example.connect4.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface ScoreDatabaseDao {

    @Insert
    suspend fun insert(score: Score)

    @Update
    suspend fun update(score: Score)

    @Query("SELECT * from connect4_score_table WHERE scoreId = :key")
    suspend fun get(key: Long): Score?

    @Query("DELETE FROM connect4_score_table")
    suspend fun clear()

    @Query("SELECT * FROM connect4_score_table ORDER BY scoreId DESC LIMIT 1")
    suspend fun getLastScore(): Score?

    @Query("SELECT * FROM connect4_score_table ORDER BY scoreId DESC")
    fun getAllScores(): LiveData< List<Score> >

}