package com.example.newsapplications.Repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope


import com.example.newsapplications.Models.Article

import kotlinx.coroutines.launch


class NewsViewModel(private val repository: NewsRepository) : ViewModel() {

    private val _newsList = MutableLiveData<List<Article>>()
    val newsList: LiveData<List<Article>> = _newsList

    private val _selectedArticle = MutableLiveData<Article?>()
    val selectedArticle: LiveData<Article?> = _selectedArticle

    fun fetchNews() {
        viewModelScope.launch {
            val news = repository.getNews()
            _newsList.postValue(news ?: emptyList())
        }
    }
    fun selectArticle(article: Article) {
        _selectedArticle.postValue(article)
    }
}

