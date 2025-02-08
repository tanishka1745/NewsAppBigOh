package com.example.newsapplications.Repository

import android.util.Log


import com.example.newsapplications.Models.Article
import com.example.newsapplications.Retrofit.NewsAPI


class NewsRepository(private val newsApi: NewsAPI) {


    suspend fun getNews(s: String, s1: String, currentPage: Int): List<Article>? {
        return try {
            val response = newsApi.getNews("us","455a09ecdbc245bb9bbd0ea3d1d07975",1)
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



