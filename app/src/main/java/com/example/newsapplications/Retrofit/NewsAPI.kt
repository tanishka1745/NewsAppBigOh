package com.example.newsapplications.Retrofit

import com.example.newsapplications.Models.NewsResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET

import retrofit2.http.Query

interface NewsAPI
{
    @GET("v2/top-headlines")
    suspend fun getNews(
        @Query("country") country: String,
        @Query("apiKey") apiKey: String,
        @Query("page") page: Int
    ): Response<NewsResponse>


    @GET("v2/top-headlines")
    suspend fun getCategoryNews(
        @Query("country") country: String,
        @Query("category") category: String,
        @Query("apiKey") apiKey: String,
        @Query("page") page: Int
    ): Response<NewsResponse>


}