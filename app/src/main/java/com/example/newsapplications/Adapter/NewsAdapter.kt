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



    fun updateNews(articleArticle: List<Article>) {
        val oldSize = articleList.size
        articleList.addAll(articleArticle)
        notifyItemRangeInserted(oldSize, articleArticle.size)  // Notify only new items
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

        Glide.with(holder.itemView).load(article.urlToImage).into(holder.articleImage)
        holder.articleTitle.text = article.title
        holder.articleDescription.text = article.description
        holder.articleDateTime.text = article.publishedAt



        holder.itemView.setOnClickListener {
            onItemClick(article)
        }
    }

    fun setArticles(articles: List<Article>) {
        this.articleList = articles.toMutableList()
        notifyDataSetChanged()
    }
}

