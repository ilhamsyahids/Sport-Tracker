package com.myandroid.sporttracker.db

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.room.TypeConverter
import java.io.ByteArrayOutputStream
import java.lang.Integer.parseInt

class Converters {

    @TypeConverter
    fun fromBitmap(bmp: Bitmap): ByteArray {
        val outputStream = ByteArrayOutputStream()
        bmp.compress(Bitmap.CompressFormat.PNG, 100, outputStream)

        return outputStream.toByteArray()
    }

    @TypeConverter
    fun toBitmap(byteArray: ByteArray): Bitmap {
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }

    @TypeConverter
    fun toSportType(value: String) = enumValueOf<SportType>(value)

    @TypeConverter
    fun fromSportType(value: SportType) = value.name

    @TypeConverter
    fun fromFrequency(frequency: Frequency): String {
        return frequency.ordinal.toString()
    }

    @TypeConverter
    fun toFrequency(number: String): Frequency {
        return try {
            Frequency.values()[parseInt(number)]
        }
        catch(exception: NumberFormatException) {
            Frequency.Daily
        }
    }

    @TypeConverter
    fun fromArray(numbers: ArrayList<Int?>): String {
        return try {
            val stringBuilder = StringBuilder()
            for (s in numbers) {
                stringBuilder.append(s.toString())
                stringBuilder.append(",")

            }
            stringBuilder.toString()
        } catch (e: NullPointerException) {
            ""
        }
    }

    @TypeConverter
    fun toArray(concatenatedStrings: String): ArrayList<Int> {
        val list = ArrayList<Int>()
        val array = concatenatedStrings.split(",").toTypedArray()
        try{
            for (index in 0..array.size)
                list.add(parseInt(array[index]))
        }
        catch(exception: NumberFormatException){
            return list
        }
        return list
    }
}