package com.myandroid.sporttracker.util

import android.content.Context
import android.text.format.DateFormat
import android.util.Log
import java.text.DateFormatSymbols
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object TimeDateUtil {

    fun getTimestampInSeconds(timeString: String?): Long {
        val format = if (timeString?.contains("am".toRegex()) == false && !timeString.contains("pm".toRegex()))
            SimpleDateFormat("HH:mm", Locale.ENGLISH)
        else
            SimpleDateFormat("hh:mm a", Locale.ENGLISH)
        return try {
            val date = format.parse(timeString ?: "null")
            date.time / 1000
        } catch (e: ParseException) {
            Log.v("ParseException", e.message ?: "error")
            172800
        }
    }

    fun formatTimeInMilliseconds(timeInMilliseconds: Long): String {
        if (timeInMilliseconds != 172800000L) {
            val sdf = SimpleDateFormat("HH:mm", Locale.ENGLISH)
            return sdf.format(timeInMilliseconds)
        }
        return "Time Not Set"
    }

    fun getMonthName(month: Int): String {
        return DateFormatSymbols().months[month]
    }
}