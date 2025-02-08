package com.example.newsapplications.Fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapplications.Adapter.NewsAdapter
import com.example.newsapplications.R
import com.example.newsapplications.Repository.NewsRepository

import com.example.newsapplications.Repository.NewsViewModel
import com.example.newsapplications.Repository.NewsViewModelFactory
import com.example.newsapplications.Retrofit.RetrofitInstance
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup



class HomeFragment : Fragment() {

    private lateinit var viewModel: NewsViewModel
    private lateinit var newsAdapter: NewsAdapter
    private lateinit var progressbar: ProgressBar
    private lateinit var chipGroup: ChipGroup

    private val selectedCategories = mutableListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize views
        progressbar = view.findViewById(R.id.progressBar)
        chipGroup = view.findViewById(R.id.chipGroupFilters)

        // Initialize ViewModel
        val api = RetrofitInstance.api
        val repository = NewsRepository(api)
        viewModel = ViewModelProvider(
            requireActivity(),
            NewsViewModelFactory(repository)
        )[NewsViewModel::class.java]

        // Initialize adapter
        newsAdapter = NewsAdapter { article ->
            viewModel.selectArticle(article)
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, ArticleFragment())
                .addToBackStack(null)
                .commit()
        }

        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerNews)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = newsAdapter

        viewModel.newsList.observe(viewLifecycleOwner) { articles ->
            articles?.let {
                if (selectedCategories.isEmpty()) {
                    newsAdapter.setArticles(it) // Replace all news if no category is selected
                } else {
                    newsAdapter.updateNews(it) // Append new articles for selected categories
                }
                progressbar.visibility = View.GONE // Hide progress bar
            }
        }
        progressbar.visibility = View.VISIBLE
        viewModel.loadNews()

        setupChipClickListener()


        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount && firstVisibleItemPosition >= 0) {
                    progressbar.visibility = View.VISIBLE
                    viewModel.loadNews(selectedCategories) // Fetch news for selected categories
                }
            }
        })
    }


private fun setupChipClickListener() {
    for (i in 0 until chipGroup.childCount) {
        val chip = chipGroup.getChildAt(i) as Chip
        chip.setOnCheckedChangeListener { _, isChecked ->
            val category = chip.text.toString().lowercase()

            if (isChecked) {
                if (!selectedCategories.contains(category)) {
                    chip.setChipBackgroundColorResource(R.color.red);
                    selectedCategories.add(category) // Add category if checked
                }
            } else {
                chip.setChipBackgroundColorResource(R.color.white);
                selectedCategories.remove(category) // Remove category if unchecked
            }

            Log.d("ChipSelection", "Selected Categories: $selectedCategories")

            // Load news based on selected categories, or all news if none selected
            if (selectedCategories.isEmpty())
            {
                Log.d("NewsFetching", "Fetching all news")
                viewModel.loadNews()
            } else {
                Log.d("NewsFetching", "Fetching news for categories: $selectedCategories")
                viewModel.loadNews(selectedCategories)
            }
        }
    }
}

}
