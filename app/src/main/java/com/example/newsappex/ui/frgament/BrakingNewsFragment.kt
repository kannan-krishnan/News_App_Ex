package com.example.newsappex.ui.frgament

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsappex.R
import com.example.newsappex.adapter.NewsAdapter
import com.example.newsappex.ui.NewsActivity
import com.example.newsappex.ui.NewsViewModel
import com.example.newsappex.utils.Resource

/**
 * Created by #kannanpvm007 on  08/02/23.
 */
class BrakingNewsFragment : Fragment(R.layout.fragment_bracking_news) {

    lateinit var viewModel: NewsViewModel
    lateinit var newsAdapter: NewsAdapter


    private lateinit var newsRecylerView: RecyclerView
    private lateinit var pagetationLoading: ProgressBar

    val TAG = "BrakingNewsFragment"
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as NewsActivity).viewModel

        newsRecylerView = view.findViewById(R.id.rvBreakingNews)
        pagetationLoading = view.findViewById(R.id.paginationProgressBar)

        setupRecyclerView()
        newsAdapter.setOnItemClickListen { article->
            Log.d(TAG, "onViewCreated: --------------------------------------->$article")
            val bundle= Bundle().apply {
                putSerializable("article",article)
            }
            findNavController().navigate(
                R.id.action_brakingNewsFragment_to_articalFragment,
                bundle
            )
        }

        viewModel.brakingNews.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    showHideLoading(show = false)
                    response.data?.let {
                        newsAdapter.differ.submitList(it.articles)
                    }
                }
                is Resource.Error -> {
                    showHideLoading(show = false)

                    response.message?.let { message ->

                        Log.d(TAG, "onViewCreated: ")
                    }
                }
                is Resource.Loading -> {
                    showHideLoading(show = true)
                }
            }

        })
    }

    private fun showHideLoading(show: Boolean) {
        pagetationLoading.visibility = if (show) View.VISIBLE else View.GONE

    }


    private fun setupRecyclerView() {
        newsAdapter = NewsAdapter()
        newsRecylerView.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)

        }

    }
}