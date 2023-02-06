package com.example.connect4.database

import android.text.format.DateUtils
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "connect4_score_table")
data class Score(
    @PrimaryKey(autoGenerate = true)
    var scoreId: Long = 0L,

    @ColumnInfo(name = "winner")
    var winner: String = "",

    @ColumnInfo(name = "time_length")
    var timeLength: Long = 0L,

    @ColumnInfo(name = "date")
    var date: DateUtils = DateUtils()
)
