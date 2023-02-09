package com.example.newsappex.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsappex.mbos.NewsResponse
import com.example.newsappex.repo.NewsRepo
import com.example.newsappex.utils.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

/**
 * Created by #kannanpvm007 on  08/02/23.
 */
class NewsViewModel(val newsRepo: NewsRepo) : ViewModel() {

    val brakingNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()

    var pageNumber = 1

    val searchNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var searchNewsPage = 1

    init {
        getBrakingNews("IN")
    }
    fun getBrakingNews(countryCode: String) = viewModelScope.launch {

        brakingNews.postValue(Resource.Loading())

        val response = newsRepo.getBrakingNews(countryCode, pageNumber)

        brakingNews.postValue(handleResponse(response))
    }

    fun geSearchNews(query: String) = viewModelScope.launch {
        searchNews.postValue(Resource.Loading())
        val response = newsRepo.searchNews(query, searchNewsPage)
        searchNews.postValue(handleSearchNewsResponse(response))
    }
    private fun handleResponse(response: Response<NewsResponse>): Resource<NewsResponse> {

        if (response.isSuccessful) {
            response.body()?.let { result ->
                return Resource.Success(result)

            }


        }
    return Resource.Error(response.message(),null)
    }
    private fun handleSearchNewsResponse(response: Response<NewsResponse>) : Resource<NewsResponse> {
        if(response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message(),null)
    }
}