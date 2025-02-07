package com.example.newsapplications.Models

data class NewsResponse(
    val status: String,
    val totalResults: Int,
    val articles: List<Article>
)
