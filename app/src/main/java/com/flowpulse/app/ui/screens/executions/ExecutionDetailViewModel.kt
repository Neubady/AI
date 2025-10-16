package com.flowpulse.app.ui.screens.executions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flowpulse.app.domain.model.Execution
import com.flowpulse.app.domain.usecase.GetExecutionDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class ExecutionDetailViewModel @Inject constructor(
    private val getExecutionDetailUseCase: GetExecutionDetailUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ExecutionDetailUiState())
    val state: StateFlow<ExecutionDetailUiState> = _state.asStateFlow()

    fun load(instanceId: Long, executionId: Long) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            val result = getExecutionDetailUseCase(instanceId, executionId)
            _state.value = if (result.isSuccess) {
                ExecutionDetailUiState(execution = result.getOrNull())
            } else {
                ExecutionDetailUiState(error = result.exceptionOrNull()?.message)
            }
        }
    }
}

data class ExecutionDetailUiState(
    val execution: Execution? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)
