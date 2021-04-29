package com.myandroid.sporttracker.repository

import androidx.lifecycle.LiveData
import com.myandroid.sporttracker.db.Sport
import com.myandroid.sporttracker.db.SportDAO
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SportRepository @Inject constructor(private val sportDAO: SportDAO) {

    suspend fun insertSport(sport: Sport) = sportDAO.insertSport(sport)
    suspend fun deleteSport(sport: Sport) = sportDAO.deleteSport(sport)
    val allSport: LiveData<List<Sport>> = sportDAO.getAllSportByDate()
}