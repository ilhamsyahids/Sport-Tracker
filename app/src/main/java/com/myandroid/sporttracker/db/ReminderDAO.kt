package com.myandroid.sporttracker.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ReminderDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReminder(reminder: Reminder)

    @Delete
    suspend fun deleteReminder(reminder: Reminder)

    @Query("SELECT * FROM reminder_table ORDER BY timeInSeconds")
    fun getAllRemindersByTime(): LiveData<List<Reminder>>

    @Query("UPDATE reminder_table SET isEnabled =:isEnabled WHERE id ==:id")
    suspend fun setEnabled(id: Int, isEnabled: Boolean)
}