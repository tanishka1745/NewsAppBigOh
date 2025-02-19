package com.example.newsapplications.Repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope


import com.example.newsapplications.Models.Article

import kotlinx.coroutines.launch


class NewsViewModel(private val repository: NewsRepository) : ViewModel() {



    private val _newsList = MutableLiveData<MutableList<Article>>(mutableListOf())
    val newsList: LiveData<MutableList<Article>> = _newsList


    private var currentPage = 1
    private var isLoading = false

    private val _selectedArticle = MutableLiveData<Article?>()
    val selectedArticle: LiveData<Article?> = _selectedArticle



    fun loadNews()
    {
        if(isLoading)  return
        isLoading= true

        viewModelScope.launch {
            val newsArticle= repository.getNews("us","455a09ecdbc245bb9bbd0ea3d1d07975", currentPage=1)
            newsArticle?.let {
                val updatedList=  _newsList.value ?: mutableListOf()
                updatedList.addAll(it)
                _newsList.postValue(updatedList)
                currentPage++;
            }
            isLoading= false
        }
    }
    fun selectArticle(article: Article) {
        _selectedArticle.postValue(article)
    }
}


