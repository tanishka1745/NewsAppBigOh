package com.example.newsapplications.RoomDB

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.newsapplications.Models.Article


@Dao
interface ArticleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticle(article: Article)

    @Query("SELECT * FROM articles")
    fun getAllArticles(): LiveData<List<Article>>


    @Query("SELECT * FROM articles WHERE title = :title LIMIT 1")
    fun getArticleByTitle(title: String): Article?


    @Query("DELETE FROM articles WHERE title = :title")
    suspend fun deleteByTitle(title: String)

    @Query("SELECT EXISTS(SELECT * FROM articles WHERE title = :title)")
    fun isArticleSaved(title: String): LiveData<Boolean>
}
