package com.myandroid.sporttracker.module

import android.content.Context
import android.content.Context.MODE_PRIVATE
import androidx.room.Room
import com.myandroid.sporttracker.api.NewsApi
import com.myandroid.sporttracker.db.WorkoutDatabase
//import com.myandroid.sporttracker.repository.NewsRepository
import com.myandroid.sporttracker.util.Constant.BASE_URL
import com.myandroid.sporttracker.util.Constant.SPORT_DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideWorkoutDatabase(@ApplicationContext app: Context) =
        Room.databaseBuilder(app, WorkoutDatabase::class.java, SPORT_DATABASE_NAME).build()

    @Singleton
    @Provides
    fun provideSportDao(db: WorkoutDatabase) = db.getSportDao()

    @Singleton
    @Provides
    fun provideReminderDao(db: WorkoutDatabase) = db.getReminderDao()

    @Singleton
    @Provides
    fun provideArticleDao(db: WorkoutDatabase) = db.getArticleDao()

    @Singleton
    @Provides
    fun provideSharedPreferences(@ApplicationContext app: Context) =
        app.getSharedPreferences("sportTrackerSharedPref", MODE_PRIVATE)

//    @Singleton
//    @Provides
//    fun provideNewsRepository(
//        api: NewsApi
//    ) = NewsRepository(api)

    @Singleton
    @Provides
    fun provideNewsApi(): NewsApi {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(NewsApi::class.java)
    }
}