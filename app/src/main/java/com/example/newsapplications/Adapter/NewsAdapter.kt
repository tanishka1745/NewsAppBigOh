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


class NewsAdapter(private val onItemClick: (Article) -> Unit) : RecyclerView.Adapter<NewsAdapter.ArticleViewHolder>() {

    private var articleList: MutableList<Article> = mutableListOf()

    // Updates the article list and notifies the RecyclerView of new items.
    fun updateNews(newArticles: List<Article>) {
        val oldSize = articleList.size
        articleList.addAll(newArticles)
        notifyItemRangeInserted(oldSize, newArticles.size) // Notify only for the new items
    }

    // This method resets the articles in the adapter and notifies that the data has changed.
    fun setArticles(articles: List<Article>) {
        this.articleList = articles.toMutableList()
        notifyDataSetChanged() // Notify the adapter to refresh the entire list
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

        // Load image using Glide
        Glide.with(holder.itemView)
            .load(article.urlToImage)
            .into(holder.articleImage)

        // Bind article data to views
        holder.articleTitle.text = article.title
        holder.articleDescription.text = article.description
        holder.articleDateTime.text = article.publishedAt

        // Handle item click
        holder.itemView.setOnClickListener {
            onItemClick(article)
        }
    }
}
