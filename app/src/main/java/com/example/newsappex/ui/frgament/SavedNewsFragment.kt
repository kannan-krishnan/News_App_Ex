package com.example.newsappex.ui.frgament

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.newsappex.R
import com.example.newsappex.ui.NewsActivity
import com.example.newsappex.ui.NewsViewModel

/**
 * Created by #kannanpvm007 on  08/02/23.
 */
class SavedNewsFragment:Fragment(R.layout.fragment_saved_news) {

    lateinit var viewModel: NewsViewModel
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as NewsActivity).viewModel
    }
    
}