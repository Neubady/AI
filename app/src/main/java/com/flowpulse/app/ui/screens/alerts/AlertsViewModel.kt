package com.flowpulse.app.ui.screens.alerts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flowpulse.app.domain.model.Alert
import com.flowpulse.app.domain.repo.FlowPulseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@HiltViewModel
class AlertsViewModel @Inject constructor(
    private val repository: FlowPulseRepository
) : ViewModel() {

    private val _state = MutableStateFlow(AlertsUiState())
    val state: StateFlow<AlertsUiState> = _state.asStateFlow()

    fun observe(instanceId: Long) {
        viewModelScope.launch {
            repository.observeAlerts(instanceId).collectLatest { alerts ->
                _state.value = _state.value.copy(alerts = alerts)
            }
        }
    }
}

data class AlertsUiState(
    val alerts: List<Alert> = emptyList()
)
