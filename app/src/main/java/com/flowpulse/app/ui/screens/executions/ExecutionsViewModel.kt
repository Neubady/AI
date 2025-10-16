package com.flowpulse.app.ui.screens.executions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flowpulse.app.domain.model.Execution
import com.flowpulse.app.domain.usecase.DeleteExecutionUseCase
import com.flowpulse.app.domain.usecase.ObserveExecutionsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@HiltViewModel
class ExecutionsViewModel @Inject constructor(
    private val observeExecutionsUseCase: ObserveExecutionsUseCase,
    private val deleteExecutionUseCase: DeleteExecutionUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ExecutionsUiState())
    val state: StateFlow<ExecutionsUiState> = _state.asStateFlow()

    fun observe(instanceId: Long) {
        viewModelScope.launch {
            observeExecutionsUseCase(instanceId).collectLatest { executions ->
                _state.value = _state.value.copy(executions = executions)
            }
        }
        viewModelScope.launch { observeExecutionsUseCase.refresh(instanceId) }
    }

    fun delete(instanceId: Long, executionId: Long) {
        viewModelScope.launch {
            val result = deleteExecutionUseCase(instanceId, executionId)
            _state.value = _state.value.copy(error = result.exceptionOrNull()?.message)
        }
    }
}

data class ExecutionsUiState(
    val executions: List<Execution> = emptyList(),
    val error: String? = null
)
