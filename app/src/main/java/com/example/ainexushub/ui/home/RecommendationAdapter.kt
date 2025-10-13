package com.example.ainexushub.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.ainexushub.R
import com.example.ainexushub.databinding.ItemRecommendationBinding
import com.example.ainexushub.model.Recommendation

class RecommendationAdapter : RecyclerView.Adapter<RecommendationAdapter.RecommendationViewHolder>() {

    private val items = mutableListOf<Recommendation>()

    fun submitList(data: List<Recommendation>) {
        items.clear()
        items.addAll(data)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommendationViewHolder {
        val binding = ItemRecommendationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecommendationViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: RecommendationViewHolder, position: Int) {
        holder.bind(items[position])
    }

    inner class RecommendationViewHolder(private val binding: ItemRecommendationBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Recommendation) {
            binding.recommendationTitle.text = item.title
            binding.recommendationDescription.text = item.description
            binding.recommendationImage.load(item.image) {
                placeholder(R.drawable.ic_splash_logo)
                error(R.drawable.ic_splash_logo)
            }
        }
    }
}
