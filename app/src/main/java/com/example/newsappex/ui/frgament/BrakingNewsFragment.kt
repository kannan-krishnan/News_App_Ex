package com.example.newsappex.ui.frgament

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AbsListView
import android.widget.ProgressBar
import android.widget.Toast
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

/**
 * Created by #kannanpvm007 on  08/02/23.
 */
class BrakingNewsFragment : Fragment(R.layout.fragment_bracking_news) {

    lateinit var viewModel: NewsViewModel
    lateinit var newsAdapter: NewsAdapter


    private lateinit var newsRecyclerView: RecyclerView
    private lateinit var paginationLoading: ProgressBar
    var isLoading = false
    var isScrolling = false
    var isLastPage = false
    val TAG = "BrakingNewsFragment"
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as NewsActivity).viewModel

        newsRecyclerView = view.findViewById(R.id.rvBreakingNews)
        paginationLoading = view.findViewById(R.id.paginationProgressBar)



        setupRecyclerView()
        newsAdapter.setOnItemClickListen { article ->
            Log.d(TAG, "onViewCreated: --------------------------------------->$article")
            val bundle = Bundle().apply {
                putSerializable("article", article)
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
                        newsAdapter.differ.submitList(it.articles.toList())

                        val totalPage=it.totalResults/Constants.PAGE_ITEM_PER_API_CAll+2
                        Log.e(TAG, "onViewCreated: t.totalResults:${it.totalResults} \n  Constants.PAGE_ITEM_PER_API_CAl:${Constants.PAGE_ITEM_PER_API_CAll}", )
//                        isLastPage=viewModel.pageNumber==totalPage

                        if (it.articles.size <Constants.PAGE_ITEM_PER_API_CAll ){
                            newsRecyclerView.setPadding(0,0,0,0)
                        }
                    }
                }
                is Resource.Error -> {
                    showHideLoading(show = false)

                    response.message?.let { message ->

                        Log.d(TAG, "onViewCreated: ")
                        Toast.makeText(requireContext(), "$message", Toast.LENGTH_SHORT).show()
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
                isScrolling = true
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

            val canMoveNextPage= notLoadingAndNotLastPage && isNotAtBin && isLastAtItem && totalMoreThenVisitable && isScrolling
            Log.d(TAG, "onScrolled:$notLoadingAndNotLastPage ,$isLastAtItem, $isNotAtBin, $totalMoreThenVisitable")
            if (canMoveNextPage){

                viewModel.getBrakingNews("In")
                isScrolling=false
            }

        }
    }
    private fun showHideLoading(show: Boolean) {
        paginationLoading.visibility = if (show) View.VISIBLE else View.GONE

        isLoading = show

    }


    private fun setupRecyclerView() {
        newsAdapter = NewsAdapter()
        newsRecyclerView.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
            addOnScrollListener(this@BrakingNewsFragment.scrollListener)

        }

    }
}