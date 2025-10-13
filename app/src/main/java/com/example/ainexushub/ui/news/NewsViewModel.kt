package com.example.ainexushub.ui.news

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.ainexushub.AiNexusHubApp
import com.example.ainexushub.data.repository.AiRepository
import com.example.ainexushub.model.NewsArticle
import kotlinx.coroutines.launch

class NewsViewModel(private val repository: AiRepository) : ViewModel() {

    private val _articles = MutableLiveData<List<NewsArticle>>(emptyList())
    val articles: LiveData<List<NewsArticle>> = _articles

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        refreshNews()
    }

    fun refreshNews() {
        viewModelScope.launch {
            _isLoading.value = true
            _articles.value = repository.fetchAiNews()
            _isLoading.value = false
        }
    }

    companion object {
        fun provideFactory(application: Application): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val app = application as AiNexusHubApp
                return NewsViewModel(app.repository) as T
            }
        }
    }
}
