package com.myandroid.sporttracker.repository

import com.myandroid.sporttracker.db.Sport
import com.myandroid.sporttracker.db.SportDAO
import javax.inject.Inject

class SportRepository @Inject constructor(private val sportDAO: SportDAO) {

    suspend fun insertSport(sport: Sport) = sportDAO.insertSport(sport)
    suspend fun getAllSportByDate() = sportDAO.getAllSportByDate()
}