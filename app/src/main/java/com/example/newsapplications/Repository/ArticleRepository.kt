package com.example.newsapplications.Repository

import androidx.lifecycle.LiveData
import com.example.newsapplications.Models.Article
import com.example.newsapplications.RoomDB.ArticleDao

class ArticleRepository(private val articleDao: ArticleDao) {

    val allArticles: LiveData<List<Article>> = articleDao.getAllArticles()

    fun getArticleByTitle(title: String): Article? {
        return articleDao.getArticleByTitle(title)
    }

    suspend fun saveArticle(article: Article) {
        articleDao.insertArticle(article)
    }
    suspend fun deleteByTitle(title: String) {
        articleDao.deleteByTitle(title)
    }

}
