package com.myandroid.sporttracker.ui.track

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.myandroid.sporttracker.db.Sport
import com.myandroid.sporttracker.repository.SportRepository

class TrackViewModel @ViewModelInject constructor(private val sportRepository: SportRepository) : ViewModel() {

    val allSport: LiveData<List<Sport>> = sportRepository.allSport
}