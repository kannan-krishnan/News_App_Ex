package com.example.newsappex.ui.api

import com.example.newsappex.mbos.NewsResponse
import com.example.newsappex.utils.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by #kannanpvm007 on  08/02/23.
 */
interface NewsApi {

    @GET("/v2/top-headlines ")
    suspend fun getBrakingNews(@Query("country") countryCode: String="in", @Query("page") page:Int=1, @Query("apiKey") apikey:String= Constants.API_KEY) :Response<NewsResponse>

    @GET("/v2/top-everything ")
    suspend fun getSearchForNews(@Query("q") query: String, @Query("page") page:Int=1, @Query("apiKey") apikey:String= Constants.API_KEY) :Response<NewsResponse>

}