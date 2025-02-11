package com.example.newsapplications.Repository

import android.util.Log


import com.example.newsapplications.Models.Article
import com.example.newsapplications.Retrofit.NewsAPI
import com.example.newsapplications.Utils.Constant


class NewsRepository(private val newsApi: NewsAPI) {

    // Fetch general news with pagination and total count of articles
    suspend fun getNews(country: String, apiKey: String, currentPage: Int): PaginatedResponse? {
        return try {
            // Pass currentPage to get the correct page of news
            val response = newsApi.getNews(country, apiKey, currentPage)
            Log.d("NewsRepository", "API response: ${response.raw()}")

            if (response.isSuccessful) {
                val articles = response.body()?.articles
                val totalResults = response.body()?.totalResults ?: 0 // Get total results from API
                Log.d("NewsRepository", "Articles received: ${articles?.size ?: 0}, Total results: $totalResults")
                PaginatedResponse(articles, totalResults)
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

// A data class to hold articles and total count of available articles
data class PaginatedResponse(
    val articles: List<Article>?,
    val totalResults: Int // Total available articles for pagination
)



