package com.example.newsappex.mbos

data class NewsResponse(
    val articles: MutableList<Article>,
    val status: String,
    val totalResults: Int
)