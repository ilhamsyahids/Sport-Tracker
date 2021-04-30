package com.myandroid.sporttracker.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlin.collections.ArrayList


@Entity(tableName = "reminder_table")
data class Reminder(
    var name: String?,
    var info: String? = "",
    var timeInSeconds: Long = 60 * 60 * 24,
    var type: SportType? = null,
    var frequency: Frequency?,
    var isEnabled: Boolean = false,
    var day: Int = if (frequency?.ordinal == 1) 0 else -1,
    var month: Int?,
    var year: Int?,
    var customDays: ArrayList<Int?>? = ArrayList()
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
}

enum class Frequency {
    OneTime, Daily, Weekly, Monthly
}