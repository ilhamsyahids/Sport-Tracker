package com.myandroid.sporttracker.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable


@Entity(tableName = "article_table")
data class Article(
     val author: String = "",
     val content: String = "",
     val description: String = "",
     val publishedAt: String = "",
     val source: Source? = null,
     val title: String = "",
     val url: String = "",
     val urlToImage: String = ""
) : Serializable {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
}

data class NewsList(
     val articles: List<Article>,
     val status: String,
     val totalResults: Int
)

data class Source(
     val id: Any,
     val name: String
)