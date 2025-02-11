package com.example.newsapplications.Fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.HorizontalScrollView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapplications.Adapter.NewsAdapter
import com.example.newsapplications.Models.Article
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

        progressbar = view.findViewById(R.id.progressBar)

        // Initialize Retrofit API and ViewModel
        val api = RetrofitInstance.api
        val repository = NewsRepository(api)
        viewModel = ViewModelProvider(
            requireActivity(),
            NewsViewModelFactory(repository)
        )[NewsViewModel::class.java]


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

        // Observe news list changes
        viewModel.newsList.observe(viewLifecycleOwner) { articles ->
            articles?.let {
                if (selectedCategories.isEmpty()) {
                    newsAdapter.setArticles(it) // Replace all news if no category is selected
                } else {
                    applySorting(it) // Apply sorting based on selected filters
                }
                progressbar.visibility = View.GONE // Hide progress bar
            }
        }
        progressbar.visibility = View.VISIBLE
        viewModel.loadNews()

        // Setup Chip click listeners for sorting
        chipGroup = view.findViewById(R.id.chipGroupFilters)

        // Handling individual chip clicks
        val chipTitle = view.findViewById<Chip>(R.id.chipTitle)
        val chipDate = view.findViewById<Chip>(R.id.chipDate)
        val chipDescription = view.findViewById<Chip>(R.id.chipDescription)

        chipTitle.setOnClickListener {
            toggleChipSelection(chipTitle, "Title")
            applySorting(viewModel.newsList.value ?: emptyList())
        }

        chipDate.setOnClickListener {
            toggleChipSelection(chipDate, "Date")
            applySorting(viewModel.newsList.value ?: emptyList())
        }

        chipDescription.setOnClickListener {
            toggleChipSelection(chipDescription, "Description")
            applySorting(viewModel.newsList.value ?: emptyList())
        }

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount && firstVisibleItemPosition >= 0) {
                    // Check if more data is available
                    if (viewModel.hasMoreData()) {
                        progressbar.visibility = View.VISIBLE
                        // Load next page of news
                        viewModel.loadNews(viewModel.currentPage + 1)
                    } else {
                        // No more data, hide progress bar
                        progressbar.visibility = View.GONE
                    }
                }
            }
        })
    }

    private fun toggleChipSelection(chip: Chip, category: String) {
        if (selectedCategories.contains(category)) {
            selectedCategories.remove(category)
            chip.setChipBackgroundColorResource(android.R.color.white)
        } else {
            selectedCategories.add(category)
            chip.setChipBackgroundColorResource(R.color.red)
        }
    }

    private fun applySorting(articles: List<Article>) {
        val uniqueArticles = articles.distinctBy {
            Triple(it.title?.lowercase()?.trim(), it.description?.lowercase()?.trim(), it.publishedAt?.trim())
        }

        val sortedList = when (selectedCategories.size) {
            1 -> {
                when (selectedCategories[0]) {
                    "Title" -> uniqueArticles.sortedBy { it.title?.lowercase() ?: "" }
                    "Date" -> uniqueArticles.sortedBy { it.publishedAt ?: "" }
                    "Description" -> uniqueArticles.sortedBy { it.description?.lowercase() ?: "" }
                    else -> uniqueArticles
                }
            }
            2 -> {
                when {
                    selectedCategories.contains("Title") && selectedCategories.contains("Date") ->
                        uniqueArticles.sortedWith(compareBy({ it.title?.lowercase() ?: "" }, { it.publishedAt ?: "" }))
                    selectedCategories.contains("Title") && selectedCategories.contains("Description") ->
                        uniqueArticles.sortedWith(compareBy({ it.title?.lowercase() ?: "" }, { it.description?.lowercase() ?: "" }))
                    selectedCategories.contains("Date") && selectedCategories.contains("Description") ->
                        uniqueArticles.sortedWith(compareBy({ it.publishedAt ?: "" }, { it.description?.lowercase() ?: "" }))
                    else -> uniqueArticles
                }
            }
            else -> uniqueArticles
        }


        newsAdapter.setArticles(sortedList)
        Toast.makeText(requireContext(), "Sorted by: ${selectedCategories.joinToString(", ")}", Toast.LENGTH_SHORT).show()
    }
}
