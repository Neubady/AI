package com.flowpulse.app.ui.screens.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flowpulse.app.domain.model.InstanceMetrics
import com.flowpulse.app.domain.usecase.GetDashboardMetricsUseCase
import com.flowpulse.app.domain.usecase.ObserveExecutionsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val getDashboardMetrics: GetDashboardMetricsUseCase,
    private val executionsUseCase: ObserveExecutionsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(DashboardUiState())
    val state: StateFlow<DashboardUiState> = _state.asStateFlow()

    fun load(instanceId: Long) {
        _state.value = _state.value.copy(isLoading = true)
        viewModelScope.launch {
            val metrics = getDashboardMetrics(instanceId)
            _state.value = if (metrics.isSuccess) {
                _state.value.copy(isLoading = false, metrics = metrics.getOrNull())
            } else {
                _state.value.copy(isLoading = false, error = metrics.exceptionOrNull()?.message)
            }
            executionsUseCase.refresh(instanceId)
        }
    }
}

data class DashboardUiState(
    val isLoading: Boolean = false,
    val metrics: InstanceMetrics? = null,
    val error: String? = null
)
