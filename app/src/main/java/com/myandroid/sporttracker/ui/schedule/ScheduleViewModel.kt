package com.myandroid.sporttracker.ui.schedule

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myandroid.sporttracker.db.Reminder
import com.myandroid.sporttracker.db.Sport
import com.myandroid.sporttracker.repository.ReminderRepository
import com.myandroid.sporttracker.repository.SportRepository
import kotlinx.coroutines.launch

class ScheduleViewModel @ViewModelInject constructor(private val reminderRepository: ReminderRepository) : ViewModel() {

    var selectedReminder: Reminder? = null

    private val _text = MutableLiveData<String>().apply {
        value = "This is schedule Fragment"
    }
    val text: LiveData<String> = _text

    fun insertReminder(reminder: Reminder) = viewModelScope.launch {
        reminderRepository.insertReminder(reminder)
    }

    fun getReminders(): LiveData<List<Reminder>> {
        return reminderRepository.getAllRemindersByTime()
    }

    val reminderByDate = getReminders()
}