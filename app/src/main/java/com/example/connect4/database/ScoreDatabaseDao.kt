package com.example.connect4.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface ScoreDatabaseDao {

    @Insert
    fun insert(score: Score)

    @Update
    fun update(score: Score)

    @Query("SELECT * from connect4_score_table WHERE scoreId = :key")
    fun get(key: Long): Score?

    @Query("DELETE FROM connect4_score_table")
    fun clear()

    @Query("SELECT * FROM connect4_score_table ORDER VY coreId DESC LIMIT 1")
    fun getLast(): Score?

    @Query("SELECT * FROM connect4_score_table ORDER BY scoreID DESC")
    fun getAllNights(): LiveData< List<Score> >



}