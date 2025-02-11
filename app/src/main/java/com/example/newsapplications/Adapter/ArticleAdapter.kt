package com.example.newsapplications.Adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newsapplications.Models.Article
import com.example.newsapplications.R
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone


class ArticleAdapter(
    private val articles: List<Article>,
    private val onItemClick: (Article) -> Unit
) : RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder>() {

    fun formatDateTime(dateTime: String?): String {
        if (dateTime.isNullOrEmpty()) return "Unknown Date"

        return try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
            inputFormat.timeZone = TimeZone.getTimeZone("UTC") //

            val outputFormat = SimpleDateFormat("MMM dd, yyyy | hh:mm a", Locale.getDefault())
            val date = inputFormat.parse(dateTime)

            date?.let { outputFormat.format(it) } ?: "Unknown Date"
        } catch (e: Exception) {
            "Unknown Date"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_news, parent, false)
        return ArticleViewHolder(view)
    }


    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = articles[position]
        holder.titleTextView.text = article.title
        holder.descriptionTextView.text = article.description
        //holder.publishedAtTextView.text = article.publishedAt
        holder.publishedAtTextView.text = formatDateTime(article.publishedAt)
        Glide.with(holder.itemView.context).load(article.urlToImage).into(holder.articleImageView)

        // Set click listener
        holder.itemView.setOnClickListener {
            onItemClick(article)
        }
    }

    override fun getItemCount() = articles.size

    class ArticleViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titleTextView: TextView = view.findViewById(R.id.articleTitle)
        val descriptionTextView: TextView = view.findViewById(R.id.articleDescription)
        val publishedAtTextView: TextView = view.findViewById(R.id.articleDateTime)
        val articleImageView: ImageView = view.findViewById(R.id.articleImage)
    }
}
