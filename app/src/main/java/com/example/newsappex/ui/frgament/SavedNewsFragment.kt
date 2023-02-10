package com.example.newsappex.ui.frgament

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsappex.R
import com.example.newsappex.adapter.NewsAdapter
import com.example.newsappex.ui.NewsActivity
import com.example.newsappex.ui.NewsViewModel
import com.google.android.material.snackbar.Snackbar

/**
 * Created by #kannanpvm007 on  08/02/23.
 */
class SavedNewsFragment:Fragment(R.layout.fragment_saved_news) {

    lateinit var viewModel: NewsViewModel
    lateinit var newsAdapter: NewsAdapter

    private lateinit var newsRecylerView: RecyclerView
    private lateinit var pagetationLoading: ProgressBar
    private lateinit var etSearch: EditText

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as NewsActivity).viewModel


        newsRecylerView = view.findViewById(R.id.rvSavedNews)
        setupRecyclerView()


        val itemTouchHelperCallBack= object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT,
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

            val position=viewHolder.adapterPosition
                val article=newsAdapter.differ.currentList[position]
                viewModel.deleteSavedArticle(article)
                Snackbar.make(view,"SuccessFully deleted article",Snackbar.LENGTH_SHORT ).apply {
                    setAction("Unto"){
                        viewModel.savedArticle(article)
                    }
                }.show()
            }

        }
        ItemTouchHelper(itemTouchHelperCallBack).apply {
            attachToRecyclerView(newsRecylerView)
        }
        viewModel.getSavedNews().observe(viewLifecycleOwner) {
            newsAdapter.differ.submitList(it)

        }
        newsAdapter.setOnItemClickListen { article->

            val bundle= Bundle().apply {
                putSerializable("article",article)
            }
            findNavController().navigate(R.id.action_savedNewsFragment2_to_articalFragment,bundle)
        }
    }

    private fun setupRecyclerView() {
        newsAdapter = NewsAdapter()
        newsRecylerView.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)

        }


    }
}