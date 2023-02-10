package com.example.newsappex.ui

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.newsappex.mbos.Article
import com.example.newsappex.mbos.NewsResponse
import com.example.newsappex.repo.NewsRepo
import com.example.newsappex.utils.AppInit
import com.example.newsappex.utils.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

/**
 * Created by #kannanpvm007 on  08/02/23.
 */
class NewsViewModel(val newsRepo: NewsRepo, app: Application) : AndroidViewModel(app) {

    val brakingNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var breakingNewsResponse: NewsResponse? = null
    var pageNumber = 1

    val searchNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var SearchNewsResponse: NewsResponse? = null
    var searchNewsPage = 1

    val savedNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var savedNewsNewsPage = 1

    init {
        getBrakingNews("IN")
    }
    fun getBrakingNews(countryCode: String) = viewModelScope.launch {
        try {
            Log.d("TAG", "getBrakingNews: ------------------------>isNetworkEnable:${isNetworkEnable()}")
            if (isNetworkEnable()){
            brakingNews.postValue(Resource.Loading())

            val response = newsRepo.getBrakingNews(countryCode, pageNumber)

            brakingNews.postValue(handleResponse(response))
            }else{
                brakingNews.postValue( Resource.Error("No Internet Available...",null))
            }
        }catch (e:Throwable){
            when(e){
            is IOException -> brakingNews.postValue( Resource.Error("No Internet Available...",null))
            else -> brakingNews.postValue( Resource.Error("something went wrong please try again",null))
            }
}
        }


    fun geSearchNews(query: String) = viewModelScope.launch {
        Log.d("TAG", "getBrakingNews: ------------------------>isNetworkEnable:${isNetworkEnable()}")

        try {
            if (isNetworkEnable()){
        searchNews.postValue(Resource.Loading())
        val response = newsRepo.searchNews(query, searchNewsPage)
        searchNews.postValue(handleSearchNewsResponse(response))

    }else{
                searchNews.postValue( Resource.Error("No Internet Available...",null))
    }
}catch (e:Throwable){
    when(e){
        is IOException -> searchNews.postValue( Resource.Error("No Internet Available...",null))
        else -> searchNews.postValue( Resource.Error("something went wrong please try again",null))
    }
            e.printStackTrace()
}
}




    fun clearSear() {
        if (SearchNewsResponse != null) {
            SearchNewsResponse?.articles?.clear()
        }

    }

    fun savedArticle(article: Article) = viewModelScope.launch {
//        savedNews.postValue(Resource.Loading())
        val response = newsRepo.upsert(article)

    }

    fun deleteSavedArticle(article: Article) = viewModelScope.launch {
//        savedNews.postValue(Resource.Loading())
        val response = newsRepo.deleteArticle(article)

    }

    fun getSavedNews()=newsRepo.getAllSavedNews()

    private fun handleResponse(response: Response<NewsResponse>): Resource<NewsResponse> {

        if (response.isSuccessful) {
            response.body()?.let { result ->
//                if (result.articles.isNotEmpty()){
                pageNumber++

                if (breakingNewsResponse == null) {
                    breakingNewsResponse = result
                } else {
                    val old = breakingNewsResponse?.articles
                    val new = result.articles
                    old?.addAll(new)


                }
//                }

                return Resource.Success(breakingNewsResponse ?: result)

            }


        }
    return Resource.Error(response.message(),null)
    }
    private fun handleSearchNewsResponse(response: Response<NewsResponse>) : Resource<NewsResponse> {
        if(response.isSuccessful) {
            response.body()?.let { result ->


                if (result.articles.isNotEmpty()) {
                    searchNewsPage++

                    if (SearchNewsResponse == null) {
                        SearchNewsResponse = result
                    } else {
                        val old = SearchNewsResponse?.articles
                        val new = result.articles
                        old?.addAll(new)


                    }
                }

                return Resource.Success(SearchNewsResponse ?: result)
            }
        }
        return Resource.Error(response.message(), null)


    }


    fun isNetworkEnable(): Boolean {
        val connectivityManager =getApplication<AppInit>().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                when {
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                        return true
                    }
                }
            }
        } else {
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
                return true
            }
        }
return false
    }
}