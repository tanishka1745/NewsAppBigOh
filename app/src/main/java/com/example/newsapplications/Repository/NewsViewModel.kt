package com.example.newsapplications.Repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope


import com.example.newsapplications.Models.Article
import com.example.newsapplications.Utils.Constant

import kotlinx.coroutines.launch


class NewsViewModel(private val repository: NewsRepository) : ViewModel() {

    var currentPage = 1
    private var isLoading = false
    private var totalArticles: Int = 0
    private var fetchedArticles: Int = 0

    private val _selectedArticle = MutableLiveData<Article?>()
    val selectedArticle: LiveData<Article?> = _selectedArticle

    private val _newsList = MutableLiveData<List<Article>>()
    val newsList: LiveData<List<Article>> get() = _newsList

    // Function to check if more data is available
    fun hasMoreData(): Boolean {
        return fetchedArticles < totalArticles
    }

    // Function to load news
    fun loadNews(page: Int = 1) {
        if (isLoading) return
        isLoading = true

        Log.d("NewsViewModel", "Fetching general news | Page: $page")

        viewModelScope.launch {
            try {
                // Fetch general news
                val newsResponse = repository.getNews(Constant.Country, Constant.API_KEY, page)

                newsResponse?.let {
                    // Update the news list with the new articles
                    val updatedList = _newsList.value?.toMutableList() ?: mutableListOf()
                    updatedList.addAll(it.articles ?: emptyList())
                    _newsList.postValue(updatedList)

                    // Update the number of fetched articles
                    fetchedArticles += it.articles?.size ?: 0

                    // Update the total number of articles available
                    totalArticles = it.totalResults

                    // Increment page number if there is more data
                    if (hasMoreData()) {
                        currentPage++
                        Log.d("NewsViewModel", "Incrementing page number: $currentPage")
                    }
                } ?: Log.d("NewsViewModel", "No articles received")

            } catch (e: Exception) {
                Log.e("NewsViewModel", "Error fetching news: ${e.localizedMessage}")
            } finally {
                isLoading = false
            }
        }
    }

    // Function to select a specific article
    fun selectArticle(article: Article) {
        _selectedArticle.postValue(article)
    }
}
