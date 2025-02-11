package com.example.newsapplications.Adapter

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


class NewsAdapter(private val onItemClick: (Article) -> Unit) : RecyclerView.Adapter<NewsAdapter.ArticleViewHolder>() {

    private var articleList: MutableList<Article> = mutableListOf()

    // Updates the article list and notifies the RecyclerView of new items.
    fun updateNews(newArticles: List<Article>) {
        val oldSize = articleList.size
        articleList.addAll(newArticles)
        notifyItemRangeInserted(oldSize, newArticles.size)
    }

    fun setArticles(articles: List<Article>) {
        this.articleList = articles.toMutableList()
        notifyDataSetChanged()
    }
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

    inner class ArticleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val articleImage: ImageView = itemView.findViewById(R.id.articleImage)
        val articleTitle: TextView = itemView.findViewById(R.id.articleTitle)
        val articleDescription: TextView = itemView.findViewById(R.id.articleDescription)
        val articleDateTime: TextView = itemView.findViewById(R.id.articleDateTime)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_news, parent, false)
        return ArticleViewHolder(view)
    }

    override fun getItemCount(): Int = articleList.size

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = articleList[position]


        Glide.with(holder.itemView)
            .load(article.urlToImage)
            .into(holder.articleImage)


        holder.articleTitle.text = article.title
        holder.articleDescription.text = article.description
        holder.articleDateTime.text = formatDateTime(article.publishedAt)

        // Handle item click
        holder.itemView.setOnClickListener {
            onItemClick(article)
        }
    }
}
