package com.example.connect4.database

import android.text.format.DateUtils
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.connect4.getCurrentDateString
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.*

@Entity(tableName = "connect4_score_table")
data class Score(
    @PrimaryKey(autoGenerate = true)
    var scoreId: Long = 0L,

    @ColumnInfo(name = "winner")
    var winner: String = "",

    @ColumnInfo(name = "time_length")
    var timeLength: String = "",

    @ColumnInfo(name = "date")
    var date: String = getCurrentDateString()
)


