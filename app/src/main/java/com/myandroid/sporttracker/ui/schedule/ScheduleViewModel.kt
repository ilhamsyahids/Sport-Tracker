package com.myandroid.sporttracker.ui.schedule

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.myandroid.sporttracker.db.Reminder

class ScheduleViewModel : ViewModel() {

    var selectedReminder: Reminder? = null

    private val _text = MutableLiveData<String>().apply {
        value = "This is schedule Fragment"
    }
    val text: LiveData<String> = _text
}