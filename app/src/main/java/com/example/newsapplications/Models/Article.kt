package com.example.newsapplications.Models

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable


@Entity(tableName = "articles")
data class Article(
    @PrimaryKey(autoGenerate = true) val id: Long=0,
    @ColumnInfo(name = "title") val title: String?,
    @ColumnInfo(name = "description") val description: String?,
    @ColumnInfo(name = "publishedAt") val publishedAt: String?,
    @ColumnInfo(name = "urlToImage") val urlToImage: String?,
    @ColumnInfo(name = "content") val content: String?
) : Serializable