package com.example.connect4


import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.connect4.database.Score
import com.example.connect4.database.ScoreDatabase
import com.example.connect4.database.ScoreDatabaseDao
import org.junit.Assert.assertEquals
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class ScoreDatabaseTest {
    private lateinit var scoreDao: ScoreDatabaseDao
    private lateinit var db: ScoreDatabase

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        // Using an in-memory database because the information stored here disappears when the
        // process is killed.
        db = Room.inMemoryDatabaseBuilder(context, ScoreDatabase::class.java)
                // Allowing main thread queries, just for testing.
                .allowMainThreadQueries()
                .build()
        scoreDao = db.scoreDatabaseDao
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertAndGetNight() {
        val night = Score()
        scoreDao.insert(night)
        val lastMatch = scoreDao.getLast()
        assertEquals(lastMatch?.winner, "")
    }
}