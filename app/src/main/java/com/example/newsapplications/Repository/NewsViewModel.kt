package com.example.newsapplications.Repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope


import com.example.newsapplications.Models.Article

import kotlinx.coroutines.launch



class NewsViewModel(private val repository: NewsRepository) : ViewModel() {

    private val _newsList = MutableLiveData<List<Article>>()
    val newsList: LiveData<List<Article>> = _newsList

    private var currentPage = 1
    private var isLoading = false

    private val _selectedArticle = MutableLiveData<Article?>()
    val selectedArticle: LiveData<Article?> = _selectedArticle

    fun loadNews(selectedCategories: List<String> = emptyList()) {
        if (isLoading) return
        isLoading = true

        Log.d("NewsViewModel", "Fetching news... Categories: $selectedCategories | Page: $currentPage")

        viewModelScope.launch {
            try {
                val newsArticles = if (selectedCategories.isEmpty()) {
                    Log.d("NewsViewModel", "Fetching general news")
                    repository.getNews("us", "455a09ecdbc245bb9bbd0ea3d1d07975", currentPage)
                } else {
                    Log.d("NewsViewModel", "Fetching category news for: ${selectedCategories.joinToString(", ")}")
                    repository.getCategoryNews("us", selectedCategories.joinToString(","), "455a09ecdbc245bb9bbd0ea3d1d07975", currentPage)
                }

                newsArticles?.let {
                    val updatedList = _newsList.value?.toMutableList() ?: mutableListOf()
                    updatedList.addAll(it)
                    _newsList.postValue(updatedList)

                    Log.d("NewsViewModel", "Received ${it.size} articles for categories: $selectedCategories")

                    currentPage++
                    Log.d("NewsViewModel", "Incrementing page number: $currentPage")
                } ?: Log.d("NewsViewModel", "No articles received for categories: $selectedCategories")

            } catch (e: Exception) {
                Log.e("NewsViewModel", "Error fetching news: ${e.localizedMessage}")
            } finally {
                isLoading = false
            }
        }
    }

    fun selectArticle(article: Article) {
        _selectedArticle.postValue(article)
    }
}



