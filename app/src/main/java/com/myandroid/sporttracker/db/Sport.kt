package com.myandroid.sporttracker.db

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sport_table")
data class Sport(
        var image: Bitmap? = null,
        var timestamp: Long = 0L,
        var distanceInMeters: Int = 0,
        var timeInMillis: Long = 0L,
        var type: SportType? = null
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
}