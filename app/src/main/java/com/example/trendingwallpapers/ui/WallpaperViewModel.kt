package com.example.trendingwallpapers.ui

import androidx.lifecycle.ViewModel
import com.example.trendingwallpapers.data.WallpaperRepository

class WallpaperViewModel : ViewModel() {
    val uiState = WallpaperUiState(
        wallpapers = WallpaperRepository.trendingWallpapers
    )
}
