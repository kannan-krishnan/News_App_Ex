package com.example.newsappex.repo

import com.example.newsappex.db.ArticleDatabase
import com.example.newsappex.ui.api.RetrofitInstance

class NewsRepo(
    val db:ArticleDatabase,

    ) {

suspend fun getBrakingNews(cuntryCode:String,page:Int)=
    RetrofitInstance.api.getBrakingNews(cuntryCode,page)

}
