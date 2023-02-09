package com.example.newsappex.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newsappex.R
import com.example.newsappex.mbos.Article

/**
 * Created by #kannanpvm007 on  08/02/23.
 */
class NewsAdapter : RecyclerView.Adapter<NewsAdapter.ArticleViewHolder>() {


    inner class ArticleViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview)

    private val differCallable = object : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }

    }
    val differ = AsyncListDiffer(this, differCallable)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {

        return ArticleViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_article_preview,parent,false))

    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {

        val article=differ.currentList[position]


        holder.itemView.apply {
            Glide.with(this).load(article.urlToImage).into(this.findViewById(R.id.ivArticleImage))

            this.findViewById<TextView>(R.id.tvSource).text=article.source.name
            this.findViewById<TextView>(R.id.tvTitle).text=article.title
            this.findViewById<TextView>(R.id.tvDescription).text=article.description
            this.findViewById<TextView>(R.id.tvPublishedAt).text=article.publishedAt
            setOnItemClickListen {
                onItemClickListener?.let { it(article) }

            }
        }


    }

    override fun getItemCount(): Int {
        return  differ.currentList.size
    }

    private var onItemClickListener:((Article)->Unit)?=null

    fun setOnItemClickListen(listener: (Article) ->Unit){
        onItemClickListener=listener
    }
}