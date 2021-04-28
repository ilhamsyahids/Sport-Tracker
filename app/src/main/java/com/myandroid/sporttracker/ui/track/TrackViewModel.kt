package com.myandroid.sporttracker.ui.track

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myandroid.sporttracker.db.Sport
import com.myandroid.sporttracker.repository.SportRepository
import kotlinx.coroutines.launch

class TrackViewModel @ViewModelInject constructor(private val sportRepository: SportRepository) : ViewModel() {

    fun getAllSport() = viewModelScope.launch {
        sportRepository.getAllSportByDate()
    }
}