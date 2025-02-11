package com.example.newsapplications.Repository


import android.icu.text.CaseMap.Title
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.newsapplications.Models.Article
import kotlinx.coroutines.Dispatchers

import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ArticleViewModel(private val repository: ArticleRepository) : ViewModel() {

    val allArticles = repository.allArticles

        fun saveArticle(article: Article)
        {
        viewModelScope.launch {
            repository.saveArticle(article)
        }
    }
    fun deleteByTitle(title: String) {
        viewModelScope.launch {
            repository.deleteByTitle(title)
        }
    }
    fun isArticleSavedByTitle(title: String): LiveData<Boolean> {
        return liveData {
            val result = withContext(Dispatchers.IO) {
                repository.isArticleSavedByTitle(title)
            }
            emit(result)     //updating in background
        }
    }

}
