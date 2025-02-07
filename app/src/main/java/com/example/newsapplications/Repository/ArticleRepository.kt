package com.example.newsapplications.Repository

import androidx.lifecycle.LiveData
import com.example.newsapplications.Models.Article
import com.example.newsapplications.RoomDB.ArticleDao

class ArticleRepository(private val articleDao: ArticleDao) {

    val allArticles: LiveData<List<Article>> = articleDao.getAllArticles()

    suspend fun saveArticle(article: Article) {
        articleDao.insertArticle(article)
    }
    fun isArticleSaved(title: String): LiveData<Boolean> {
        return articleDao.isArticleSaved(title)
    }
}
