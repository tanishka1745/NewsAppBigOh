package com.example.newsapplications.Repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapplications.Models.Article
import kotlinx.coroutines.launch

class ArticleViewModel(private val repository: ArticleRepository) : ViewModel() {

    val allArticles = repository.allArticles

    fun saveArticle(article: Article) {
        viewModelScope.launch {
            repository.saveArticle(article)
        }
    }
    fun isArticleSaved(title: String): LiveData<Boolean> {
        return repository.isArticleSaved(title)
    }
}
