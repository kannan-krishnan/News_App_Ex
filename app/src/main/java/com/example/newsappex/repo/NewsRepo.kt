package com.example.newsappex.repo

import com.example.newsappex.db.ArticleDatabase
import com.example.newsappex.mbos.Article
import com.example.newsappex.ui.api.RetrofitInstance

class NewsRepo(
    val db:ArticleDatabase,

    ) {

suspend fun getBrakingNews(cuntryCode:String,page:Int)=
    RetrofitInstance.api.getBrakingNews(cuntryCode,page)

    suspend fun searchNews(query:String, pageNumber: Int)=RetrofitInstance.api.getSearchForNews(query,pageNumber)

    suspend fun upsert(article: Article)= db.getArticleDao().upsert(article)

    fun getAllSavedNews()=db.getArticleDao().getAllArticle()

   suspend fun deleteArticle(article: Article)=db.getArticleDao().deleteArticle(article)
}
