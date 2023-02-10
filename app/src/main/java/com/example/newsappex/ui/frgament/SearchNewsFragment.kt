package com.example.newsappex.ui.frgament

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AbsListView
import android.widget.EditText
import android.widget.ProgressBar
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsappex.R
import com.example.newsappex.adapter.NewsAdapter
import com.example.newsappex.ui.NewsActivity
import com.example.newsappex.ui.NewsViewModel
import com.example.newsappex.utils.Constants
import com.example.newsappex.utils.Resource
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Created by #kannanpvm007 on  08/02/23.
 */
class SearchNewsFragment:Fragment(R.layout.fragment_search_news) {

    lateinit var viewModel: NewsViewModel
    lateinit var newsAdapter: NewsAdapter

    private lateinit var newsRecyclerView: RecyclerView
    private lateinit var paginationLoading: ProgressBar
    private lateinit var etSearch: EditText
    val TAG="SearchNewsFragment"

    var isLoading = false
    var isScrooling = false
    var isLastPage = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as NewsActivity).viewModel
        newsRecyclerView = view.findViewById(R.id.rvSearchNews)
        paginationLoading = view.findViewById(R.id.paginationProgressBar)
        etSearch = view.findViewById(R.id.etSearch)

        setupRecyclerView()
        newsAdapter.setOnItemClickListen { article->

            val bundle= Bundle().apply {
                putSerializable("article",article)
            }
            findNavController().navigate(R.id.action_searchNewsFragment2_to_articalFragment,bundle)
        }
        var job: Job?=null
        etSearch.addTextChangedListener {
            job?.cancel()
            job= MainScope().launch {
                delay(Constants.TIME_DELAY_IN_MLLI)

                it.let {
                    if (it.toString().isNotEmpty()){
                        viewModel.searchNewsPage=1
                        viewModel.clearSear()
                        viewModel.geSearchNews(it.toString())
                    }
                }
            }
        }
        viewModel.searchNews.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    showHideLoading(show = false)
                    response.data?.let {
                        newsAdapter.differ.submitList(it.articles)

                        if (it.articles.size <Constants.PAGE_ITEM_PER_API_CAll ){
                            newsRecyclerView.setPadding(0,0,0,0)
                        }
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
    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrooling = true
            }else{

            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val layoutManager=recyclerView.layoutManager as LinearLayoutManager
            val  findFirstVisibleItemPosition=layoutManager.findFirstVisibleItemPosition()
            val  visibleItem=layoutManager.childCount
            val itemCount=layoutManager.itemCount

            val notLoadingAndNotLastPage= !isLoading && !isLastPage
            val isLastAtItem=findFirstVisibleItemPosition+visibleItem >= itemCount
            val isNotAtBin=findFirstVisibleItemPosition>=0
            val totalMoreThenVisitable= itemCount >=  Constants.PAGE_ITEM_PER_API_CAll

            val canMoveNextPage= notLoadingAndNotLastPage && isNotAtBin && isLastAtItem && totalMoreThenVisitable && isScrooling
            Log.d(TAG, "onScrolled:$notLoadingAndNotLastPage ,$isLastAtItem, $isNotAtBin, $totalMoreThenVisitable")
            if (canMoveNextPage){

                viewModel.geSearchNews(etSearch.text.toString())
                isScrooling=false
            }

        }
    }

    private fun showHideLoading(show: Boolean) {
        paginationLoading.visibility = if (show) View.VISIBLE else View.GONE

    }


    private fun setupRecyclerView() {
        newsAdapter = NewsAdapter()
        newsRecyclerView.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
            addOnScrollListener(this@SearchNewsFragment.scrollListener)
        }


    }


}