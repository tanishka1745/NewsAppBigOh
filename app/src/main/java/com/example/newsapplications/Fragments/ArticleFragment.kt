package com.example.newsapplications.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.newsapplications.Models.Article
import com.example.newsapplications.R
import com.example.newsapplications.Repository.ArticleRepository
import com.example.newsapplications.Repository.ArticleViewModel
import com.example.newsapplications.Repository.ArticleViewModelFactory
import com.example.newsapplications.Repository.NewsViewModel
import com.example.newsapplications.RoomDB.ArticleDatabase
import com.google.android.material.floatingactionbutton.FloatingActionButton


class ArticleFragment : Fragment() {

    private lateinit var viewModel: ArticleViewModel
    private lateinit var viewModel2: NewsViewModel
    private var displayedArticle: Article? = null
    private var isFromSaveFragment: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_article2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get the flag to check if it's from SaveFragment
        isFromSaveFragment = arguments?.getBoolean("isFromSaveFragment", false) ?: false

        val articleImage: ImageView = view.findViewById(R.id.articleImage)
        val articleTitle: TextView = view.findViewById(R.id.articleTitle)
        val articleDescription: TextView = view.findViewById(R.id.articleDescription)
        val articleDateTime: TextView = view.findViewById(R.id.articleDateTime)
        val fab: FloatingActionButton = view.findViewById(R.id.fab)


        viewModel2 = ViewModelProvider(requireActivity())[NewsViewModel::class.java]

        if (!isFromSaveFragment) {

            viewModel2.selectedArticle.observe(viewLifecycleOwner) { article ->
                article?.let {
                    Glide.with(requireContext()).load(it.urlToImage).into(articleImage)
                    articleTitle.text = it.title
                    articleDescription.text = it.description
                    articleDateTime.text = it.publishedAt

                    displayedArticle = it


                }
            }
        } else {

            val title = arguments?.getString("title") ?: ""
            val description = arguments?.getString("description") ?: ""
            val urlToImage = arguments?.getString("urlToImage") ?: ""
            val publishedAt = arguments?.getString("publishedAt") ?: ""
            val content = arguments?.getString("content") ?: ""

            articleTitle.text = title
            articleDescription.text = description
            articleDateTime.text = publishedAt
            Glide.with(requireContext()).load(urlToImage).into(articleImage)

            displayedArticle = Article(
                title = title,
                description = description,
                urlToImage = urlToImage,
                publishedAt = publishedAt,
                content = content
            )
            fab.visibility= View.GONE
        }


        val articleDao = ArticleDatabase.getDatabase(requireContext()).articleDao()
        val repository = ArticleRepository(articleDao)
        val viewModelFactory = ArticleViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(ArticleViewModel::class.java)


//        viewModel.isArticleSaved(displayedArticle?.title ?: "").observe(viewLifecycleOwner) { saved ->
//            if (saved) {
//                fab.visibility = View.GONE
//            } else {
//                fab.visibility = View.VISIBLE
//            }
//        }


        fab.setOnClickListener {
            displayedArticle?.let { article ->
                viewModel.saveArticle(article)
                Toast.makeText(requireContext(), "Article Saved!", Toast.LENGTH_SHORT).show()
                fab.visibility = View.GONE  // Hide FAB after saving
            } ?: Toast.makeText(requireContext(), "No article to save", Toast.LENGTH_SHORT).show()
        }
    }
}
