package com.example.newsappex.ui.frgament

import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.newsappex.R
import com.example.newsappex.ui.NewsActivity
import com.example.newsappex.ui.NewsViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar

/**
 * Created by #kannanpvm007 on  08/02/23.
 */
class ArticleFragment:Fragment(R.layout.fragment_article) {

    lateinit var  viewModel: NewsViewModel
    val args : ArticleFragmentArgs by navArgs()

    val TAG="ArticleFragment"
    private lateinit var webView:WebView
    private lateinit var fab:FloatingActionButton
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel= (activity as NewsActivity).viewModel

        webView=view.findViewById(R.id.webView)
        fab=view.findViewById(R.id.fab)
        val article= args.article
        Log.d(TAG, "onViewCreated: ---------------------> $article")
        webView.apply {
            webViewClient= WebViewClient()
            article?.url?.let { loadUrl(it) }
        }
        fab.setOnClickListener {
            viewModel.savedArticle(article)
            Snackbar.make(view,"Article Saved...!",Snackbar.LENGTH_SHORT).show()
        }


    }

}