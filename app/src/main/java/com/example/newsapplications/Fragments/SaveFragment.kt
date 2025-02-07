package com.example.newsapplications.Fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapplications.Adapter.ArticleAdapter
import com.example.newsapplications.R
import com.example.newsapplications.Repository.ArticleRepository
import com.example.newsapplications.Repository.ArticleViewModel
import com.example.newsapplications.Repository.ArticleViewModelFactory
import com.example.newsapplications.RoomDB.ArticleDatabase



class SaveFragment : Fragment() {

    private lateinit var articleViewModel: ArticleViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_save2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val articleRepository = ArticleRepository(ArticleDatabase.getDatabase(requireContext()).articleDao())
        articleViewModel = ViewModelProvider(this, ArticleViewModelFactory(articleRepository)).get(ArticleViewModel::class.java)

        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerSavedNews)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        articleViewModel.allArticles.observe(viewLifecycleOwner, Observer { articles ->
            if (articles != null && articles.isNotEmpty()) {
                val adapter = ArticleAdapter(articles) { article ->
                    // When an item is clicked, pass the article data to ArticleFragment
                    val bundle = Bundle().apply {
                        putLong("id",article.id)
                        putString("title", article.title)
                        putString("description", article.description)
                        putString("urlToImage", article.urlToImage)
                        putString("publishedAt", article.publishedAt)
                        putString("content", article.content)
                        putBoolean("isFromSaveFragment", true) // Flag to indicate the source
                    }

                    // Navigate to ArticleFragment and pass the data
                    val articleFragment = ArticleFragment()
                    articleFragment.arguments = bundle
                    parentFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, articleFragment)
                        .addToBackStack(null)
                        .commit()
                }
                recyclerView.adapter = adapter
            } else {
                // Handle the case where no articles are available
                Log.d("SaveFragment", "No articles found")
            }
        })
    }
}
