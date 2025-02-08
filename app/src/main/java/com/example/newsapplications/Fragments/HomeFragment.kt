package com.example.newsapplications.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
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
    private var selectedCategory: String = "technology" // Default category

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize ViewModel to be shared across fragments
        progressbar= view.findViewById(R.id.progressBar)
        chipGroup = view.findViewById(R.id.chipGroupFilters)
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
        chipGroup.setOnCheckedChangeListener { _, checkedId ->
            val chip: Chip? = view.findViewById(checkedId)
            chip?.let {
                selectedCategory = it.text.toString().lowercase()
                //fetchNewsByCategory(selectedCategory)
            }
        }
        //fetchNewsByCategory(selectedCategory)


        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerNews)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = newsAdapter
        // Observe the news list and update the adapter
        viewModel.newsList.observe(viewLifecycleOwner) { articles ->
            if (articles != null) {
                newsAdapter.setArticles(articles)
                newsAdapter.updateNews(articles)
                progressbar.visibility= View.GONE
            }
        }
        progressbar.visibility= View.VISIBLE
        viewModel.loadNews()

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                // Load more when reaching the last item
                if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount && firstVisibleItemPosition >= 0) {
                    progressbar.visibility = View.VISIBLE
                    viewModel.loadNews()
                }
            }
        })
    }
//    private fun fetchNewsByCategory(category: String) {
//        progressbar.visibility = View.VISIBLE
//        viewModel.loadNews(category)
//    }
}

