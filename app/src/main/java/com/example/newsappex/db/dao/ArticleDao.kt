package com.example.newsappex.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.newsappex.mbos.Article

/**
 * Created by #kannanpvm007 on  08/02/23.
 */
@Dao
interface ArticleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend  fun upsert(article: Article):Long

    @Query("SELECT * FROM Article ")
    fun getAllArticle():LiveData<List<Article>>

    @Delete
    suspend fun deleteArticle(article: Article)
}