package com.myandroid.sporttracker.ui.home

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myandroid.sporttracker.db.NewsList
import com.myandroid.sporttracker.repository.NewsRepository
import com.myandroid.sporttracker.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class HomeViewModel @ViewModelInject constructor(private val newsRepository: NewsRepository) : ViewModel()  {

    val newsList: MutableLiveData<Resource<NewsList>> = MutableLiveData()
    var newsResponse: NewsList? = null

    init {
        getNewsList()
    }

    fun getNewsList(country: String = "id", category: String = "sports") = viewModelScope.launch {
        newsList.postValue(Resource.Loading())
        val res = newsRepository.getNewsList(country, category)
        newsList.postValue(handleNewsResponse(res))
    }

    private fun handleNewsResponse(response: Response<NewsList>): Resource<NewsList> {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }

}