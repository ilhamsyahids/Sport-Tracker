package com.myandroid.sporttracker.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Sport::class, Reminder::class, Article::class], version = 1)
@TypeConverters(Converters::class)
abstract class WorkoutDatabase: RoomDatabase() {

    abstract fun getSportDao(): SportDAO

    abstract fun getReminderDao(): ReminderDAO

    abstract fun getArticleDao(): ArticleDAO
}