package com.example.newsapplications.Fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapplications.Adapter.NewsAdapter
import com.example.newsapplications.R
import com.example.newsapplications.Repository.NewsRepository

import com.example.newsapplications.Repository.NewsViewModel
import com.example.newsapplications.Repository.NewsViewModelFactory
import com.example.newsapplications.Retrofit.RetrofitInstance


class HomeFragment : Fragment() {

    private lateinit var viewModel: NewsViewModel
    private lateinit var newsAdapter: NewsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize ViewModel to be shared across fragments
        val api = RetrofitInstance.api
        val repository = NewsRepository(api)
        viewModel = ViewModelProvider(requireActivity(), NewsViewModelFactory(repository))[NewsViewModel::class.java]

        newsAdapter = NewsAdapter { article ->
            // Pass selected article to ViewModel
            viewModel.selectArticle(article)

            // Navigate to ArticleFragment
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, ArticleFragment())
                .addToBackStack(null)
                .commit()
        }

        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerNews)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = newsAdapter

        // Fetch news
        viewModel.fetchNews()

        // Observe the news list and update the adapter
        viewModel.newsList.observe(viewLifecycleOwner) { articles ->
            if (articles != null) {
                newsAdapter.setArticles(articles)
            }
        }
    }
}
