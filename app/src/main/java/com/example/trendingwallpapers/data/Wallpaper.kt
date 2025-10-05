package com.example.trendingwallpapers.data

data class Wallpaper(
    val id: Int,
    val title: String,
    val category: String,
    val trendingRank: Int,
    val imageUrl: String,
    val description: String,
    val photographer: String
)
