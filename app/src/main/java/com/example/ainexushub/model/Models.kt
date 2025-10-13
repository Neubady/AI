package com.example.ainexushub.model

data class AiTool(
    val name: String,
    val description: String,
    val url: String,
    val image: String,
    val isFavorite: Boolean = false
)

data class NewsArticle(
    val title: String,
    val url: String,
    val source: String,
    val publishedAt: String,
    val imageUrl: String?
)

data class Recommendation(
    val title: String,
    val description: String,
    val image: String
)
