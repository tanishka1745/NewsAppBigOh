package com.example.newsapplications.Repository

import android.util.Log


import com.example.newsapplications.Models.Article
import com.example.newsapplications.Retrofit.NewsAPI



class NewsRepository(private val newsApi: NewsAPI) {


    suspend fun getNews(): List<Article>? {
        return try {
            val response = newsApi.getNews("us", 1, "0abcd10a111e4c62a132efd3a18e0c9c")
            Log.d("NewsRepository", "API response: ${response.raw()}")

            if (response.isSuccessful) {
                val articles = response.body()?.articles
                Log.d("NewsRepository", "Articles received: ${articles?.size ?: 0}")
                articles
            } else {
                Log.e("NewsRepository", "API Error: ${response.errorBody()?.string()}")
                null
            }
        } catch (e: Exception) {
            Log.e("NewsRepository", "Exception: ${e.localizedMessage}")
            null
        }
    }
}
//class NewsRepository(private val newsApi: NewsAPI, private val articleDao: ArticleDao) {
//
//    // Fetch articles from the API
//    suspend fun getNews(): List<Article>? {
//        return try {
//            val response = newsApi.getNews("us", 1, "0abcd10a111e4c62a132efd3a18e0c9c")
//            Log.d("NewsRepository", "API response: ${response.raw()}")
//
//            if (response.isSuccessful) {
//                Log.d("NewsRepository", "Articles received: ${response.body()?.articles?.size ?: 0}")
//                response.body()?.articles
//            } else {
//                Log.e("NewsRepository", "API Error: ${response.errorBody()?.string()}")
//                null
//            }
//        } catch (e: Exception) {
//            Log.e("NewsRepository", "Exception: ${e.localizedMessage}")
//            null
//        }
//    }
//
//    // Save article to RoomDB
//    suspend fun saveArticle(article: Article) {
//        articleDao.insert(article)
//    }
//
//    // Get all saved articles from RoomDB
//    suspend fun getSavedArticles(): LiveData<List<Article>> {
//        return articleDao.getAllArticles()
//    }
//}

