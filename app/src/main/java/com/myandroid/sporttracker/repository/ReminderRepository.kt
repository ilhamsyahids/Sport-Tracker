package com.myandroid.sporttracker.repository

import com.myandroid.sporttracker.db.Reminder
import com.myandroid.sporttracker.db.ReminderDAO
import javax.inject.Inject

class ReminderRepository @Inject constructor(private val reminderDAO: ReminderDAO) {

    suspend fun insertReminder(reminder: Reminder) = reminderDAO.insertReminder(reminder)
    suspend fun deleteReminder(reminder: Reminder) = reminderDAO.deleteReminder(reminder)
    fun getAllRemindersByTime() = reminderDAO.getAllRemindersByTime()
}