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


class ArticleAdapter(
    private val articles: List<Article>,
    private val onItemClick: (Article) -> Unit
) : RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.saved_layout_item, parent, false)
        return ArticleViewHolder(view)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = articles[position]
        holder.titleTextView.text = article.title
        holder.descriptionTextView.text = article.description
        holder.publishedAtTextView.text = article.publishedAt
        Glide.with(holder.itemView.context).load(article.urlToImage).into(holder.articleImageView)

        // Set click listener
        holder.itemView.setOnClickListener {
            onItemClick(article)
        }
    }

    override fun getItemCount() = articles.size

    class ArticleViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titleTextView: TextView = view.findViewById(R.id.articleTitleSave)
        val descriptionTextView: TextView = view.findViewById(R.id.articleDescriptionSave)
        val publishedAtTextView: TextView = view.findViewById(R.id.articleDateTimeSave)
        val articleImageView: ImageView = view.findViewById(R.id.articleImageSave)
    }
}
