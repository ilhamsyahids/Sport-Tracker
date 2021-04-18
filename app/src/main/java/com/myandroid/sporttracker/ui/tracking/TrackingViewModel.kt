package com.myandroid.sporttracker.ui.tracking

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myandroid.sporttracker.db.Sport
import com.myandroid.sporttracker.repository.SportRepository
import kotlinx.coroutines.launch

class TrackingViewModel @ViewModelInject constructor(private val sportRepository: SportRepository) : ViewModel() {

    fun insertSport(sport: Sport) = viewModelScope.launch {
        sportRepository.insertSport(sport)
    }
}