package com.myandroid.sporttracker.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface SportDAO {

    @Query("SELECT * FROM sport_table ORDER BY timestamp DESC")
    fun getAllSportByDate(): LiveData<List<Sport>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSport(sport: Sport)

    @Delete
    suspend fun deleteSport(sport: Sport)
}