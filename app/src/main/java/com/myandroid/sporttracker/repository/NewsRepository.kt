 package com.myandroid.sporttracker.repository

 import com.myandroid.sporttracker.api.NewsApi
 import javax.inject.Inject

 class NewsRepository @Inject constructor(
     private val api: NewsApi
 ) {

     suspend fun getNewsList(size: Int, page: Int, country: String = "id", category: String = "sports") =
         api.getNewsList(size, page, country, category)
 }