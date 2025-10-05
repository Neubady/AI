package com.example.trendingwallpapers.ui

import com.example.trendingwallpapers.data.Wallpaper

data class WallpaperUiState(
    val wallpapers: List<Wallpaper> = emptyList(),
    val isDownloading: Boolean = false,
    val lastDownloadTitle: String? = null
)
