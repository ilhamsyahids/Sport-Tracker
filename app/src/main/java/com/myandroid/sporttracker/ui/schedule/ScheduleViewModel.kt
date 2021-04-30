package com.myandroid.sporttracker.ui.schedule

import android.app.Application
import android.content.Intent
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.myandroid.sporttracker.db.Reminder
import com.myandroid.sporttracker.db.Sport
import com.myandroid.sporttracker.repository.ReminderRepository
import com.myandroid.sporttracker.repository.SportRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ScheduleViewModel @ViewModelInject constructor(private val reminderRepository: ReminderRepository, application: Application) : AndroidViewModel(application) {

    var selectedReminder: Reminder? = null

    private val _text = MutableLiveData<String>().apply {
        value = "This is schedule Fragment"
    }
    val text: LiveData<String> = _text

    fun insertReminder(reminder: Reminder) = viewModelScope.launch {
        reminderRepository.insertReminder(reminder)
    }

    private fun getReminders(): LiveData<List<Reminder>> {
        return reminderRepository.getAllRemindersByTime()
    }

    val reminderByDate = getReminders()

    fun scheduleReminder(reminder: Reminder) {
        viewModelScope.launch(Dispatchers.IO) {
            reminder.isEnabled = true
            reminderRepository.setReminderIsEnabled(reminder)
            startServiceFromCoroutine()
        }
    }

    fun cancelScheduledReminder(reminder: Reminder) {
        viewModelScope.launch(Dispatchers.IO) {
            reminder.isEnabled = false
            reminderRepository.setReminderIsEnabled(reminder)
            startServiceFromCoroutine()
        }
    }


    private suspend fun startServiceFromCoroutine() {
        withContext(Dispatchers.Main) {
        }
    }
}