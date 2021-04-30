package com.myandroid.sporttracker.api

import com.myandroid.sporttracker.db.NewsList
import com.myandroid.sporttracker.util.Constant.NEWS_API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {

    @GET("v2/top-headlines")
    suspend fun getNewsList(
        @Query("pageSize") size: Int = 10,
        @Query("page") page: Int = 1,
        @Query("country") country: String = "id",
        @Query("category") category: String = "sports",
        @Query("apiKey") apiKey: String = NEWS_API_KEY,
    ): Response<NewsList>
}
