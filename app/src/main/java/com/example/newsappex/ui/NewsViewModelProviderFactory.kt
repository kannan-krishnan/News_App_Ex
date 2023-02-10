package com.example.newsappex.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.newsappex.repo.NewsRepo

/**
 * Created by #kannanpvm007 on  08/02/23.
 */
class NewsViewModelProviderFactory(
    private val newsRepo: NewsRepo,
      val app: Application
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NewsViewModel(newsRepo, app) as T
    }
}