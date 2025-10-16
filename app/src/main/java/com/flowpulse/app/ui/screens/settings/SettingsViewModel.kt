package com.flowpulse.app.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flowpulse.app.billing.BillingManager
import com.flowpulse.app.data.local.AppPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val preferences: AppPreferences,
    private val billingManager: BillingManager
) : ViewModel() {

    private val _state = MutableStateFlow(SettingsUiState())
    val state: StateFlow<SettingsUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            combine(preferences.theme(), preferences.language()) { theme, language -> theme to language }
                .collect { (theme, language) ->
                    _state.value = _state.value.copy(theme = theme, language = language)
                }
        }
        billingManager.connect()
    }

    fun setTheme(theme: String) {
        viewModelScope.launch { preferences.setTheme(theme) }
    }

    fun setLanguage(language: String) {
        viewModelScope.launch { preferences.setLanguage(language) }
    }
}

data class SettingsUiState(
    val theme: String = "system",
    val language: String = "es"
)
