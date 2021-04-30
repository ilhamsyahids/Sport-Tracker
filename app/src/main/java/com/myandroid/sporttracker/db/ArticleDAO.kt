package com.myandroid.sporttracker.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ArticleDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticle(article: Article)

    @Query("SELECT * FROM article_table")
    fun getAllArticles(): LiveData<List<Article>>
}