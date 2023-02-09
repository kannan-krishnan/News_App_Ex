package com.example.newsappex.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.newsappex.R
import com.example.newsappex.db.ArticleDatabase
import com.example.newsappex.repo.NewsRepo
import com.google.android.material.bottomnavigation.BottomNavigationView

class NewsActivity : AppCompatActivity() {

      lateinit var viewModel: NewsViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)



        val repo = NewsRepo(ArticleDatabase(this))
        val viewModelProviderFactory = NewsViewModelProviderFactory(repo)
        viewModel= ViewModelProvider(this,viewModelProviderFactory).get(NewsViewModel::class.java)

        val newsNavHostFragment =supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        findViewById<BottomNavigationView>(R.id.bottomNavigationView).setupWithNavController(newsNavHostFragment.navController)

                            /*or*/
//        viewModel=ViewModelProvider(this,viewModelProviderFactory).get(NewsViewModel::class.java)

    }
}