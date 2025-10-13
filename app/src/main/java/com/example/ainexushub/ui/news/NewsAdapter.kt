package com.example.ainexushub.ui.news

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.ainexushub.R
import com.example.ainexushub.databinding.ItemNewsBinding
import com.example.ainexushub.model.NewsArticle

class NewsAdapter(private val onClick: (NewsArticle) -> Unit) :
    ListAdapter<NewsArticle, NewsAdapter.NewsViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val binding = ItemNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NewsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class NewsViewHolder(private val binding: ItemNewsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(article: NewsArticle) {
            binding.newsTitleTextView.text = article.title
            binding.newsMetaTextView.text = "${article.source} • ${article.publishedAt}"
            binding.newsImageView.load(article.imageUrl ?: R.drawable.ic_splash_logo) {
                placeholder(R.drawable.ic_splash_logo)
                error(R.drawable.ic_splash_logo)
            }
            binding.root.setOnClickListener { onClick(article) }
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<NewsArticle>() {
        override fun areItemsTheSame(oldItem: NewsArticle, newItem: NewsArticle): Boolean = oldItem.url == newItem.url
        override fun areContentsTheSame(oldItem: NewsArticle, newItem: NewsArticle): Boolean = oldItem == newItem
    }
}
