package com.example.ainexushub.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.core.view.isVisible
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.ainexushub.R
import com.example.ainexushub.databinding.ItemAiToolBinding
import com.example.ainexushub.model.AiTool

class AiToolAdapter(
    private val onTryClick: (AiTool) -> Unit,
    private val onFavoriteClick: (AiTool) -> Unit,
    private val showFavorite: Boolean = true
) : ListAdapter<AiTool, AiToolAdapter.AiToolViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AiToolViewHolder {
        val binding = ItemAiToolBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AiToolViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AiToolViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class AiToolViewHolder(private val binding: ItemAiToolBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(tool: AiTool) {
            binding.toolNameTextView.text = tool.name
            binding.toolDescriptionTextView.text = tool.description
            binding.toolImageView.load(tool.image) {
                placeholder(R.drawable.ic_splash_logo)
                error(R.drawable.ic_splash_logo)
            }
            binding.tryNowButton.setOnClickListener { onTryClick(tool) }
            binding.favoriteButton.apply {
                setImageResource(if (tool.isFavorite) R.drawable.ic_star_filled else R.drawable.ic_star_outline)
                isVisible = showFavorite
                setOnClickListener { onFavoriteClick(tool) }
            }
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<AiTool>() {
        override fun areItemsTheSame(oldItem: AiTool, newItem: AiTool): Boolean = oldItem.name == newItem.name
        override fun areContentsTheSame(oldItem: AiTool, newItem: AiTool): Boolean = oldItem == newItem
    }
}
