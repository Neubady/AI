package com.example.ainexushub.ui.favorites

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.ainexushub.AiNexusHubApp
import com.example.ainexushub.data.repository.AiRepository
import com.example.ainexushub.model.AiTool
import kotlinx.coroutines.launch

class FavoritesViewModel(private val repository: AiRepository) : ViewModel() {

    val favorites: LiveData<List<AiTool>> = repository.observeFavorites().asLiveData()

    fun removeFavorite(tool: AiTool) {
        viewModelScope.launch {
            repository.removeFavorite(tool)
        }
    }

    companion object {
        fun provideFactory(application: Application): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val app = application as AiNexusHubApp
                return FavoritesViewModel(app.repository) as T
            }
        }
    }
}
