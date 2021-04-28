package com.myandroid.sporttracker.module

import android.content.Context
import android.content.Context.MODE_PRIVATE
import androidx.room.Room
import com.myandroid.sporttracker.db.WorkoutDatabase
import com.myandroid.sporttracker.util.Constant.SPORT_DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideSportDatabase(@ApplicationContext app: Context) =
        Room.databaseBuilder(app, WorkoutDatabase::class.java, SPORT_DATABASE_NAME).build()

    @Singleton
    @Provides
    fun provideSportDao(db: WorkoutDatabase) = db.getSportDao()

    @Singleton
    @Provides
    fun provideSharedPreferences(@ApplicationContext app: Context) =
        app.getSharedPreferences("sportTrackerSharedPref", MODE_PRIVATE)
}