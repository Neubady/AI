package com.example.ainexushub.ui.home

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.ainexushub.AiNexusHubApp
import com.example.ainexushub.data.repository.AiRepository
import com.example.ainexushub.model.AiTool
import com.example.ainexushub.model.Recommendation
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: AiRepository) : ViewModel() {

    private val _tools = MutableLiveData<List<AiTool>>(emptyList())
    val tools: LiveData<List<AiTool>> = _tools

    private val _recommendations = MutableLiveData<List<Recommendation>>(emptyList())
    val recommendations: LiveData<List<Recommendation>> = _recommendations

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _searchQuery = MutableLiveData("")
    val searchQuery: LiveData<String> = _searchQuery

    val filteredTools: LiveData<List<AiTool>> = MediatorLiveData<List<AiTool>>().apply {
        fun update() {
            val query = _searchQuery.value.orEmpty().lowercase()
            val list = _tools.value.orEmpty()
            value = if (query.isBlank()) list else list.filter { it.name.lowercase().contains(query) }
        }
        addSource(_tools) { update() }
        addSource(_searchQuery) { update() }
    }

    init {
        refreshTools()
        loadRecommendations()
    }

    fun refreshTools() {
        viewModelScope.launch {
            _isLoading.value = true
            val result = repository.fetchAiTools()
            _tools.value = result
            _isLoading.value = false
        }
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun toggleFavorite(tool: AiTool) {
        viewModelScope.launch {
            if (tool.isFavorite) {
                repository.removeFavorite(tool)
            } else {
                repository.addFavorite(tool)
            }
            refreshTools()
        }
    }

    private fun loadRecommendations() {
        viewModelScope.launch {
            _recommendations.value = repository.loadRecommendations()
        }
    }

    companion object {
        fun provideFactory(application: Application): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val app = application as AiNexusHubApp
                return HomeViewModel(app.repository) as T
            }
        }
    }
}
