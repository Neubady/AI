package com.flowpulse.app.ui.screens.workflows

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flowpulse.app.domain.model.Workflow
import com.flowpulse.app.domain.usecase.ExecuteWorkflowWebhookUseCase
import com.flowpulse.app.domain.usecase.ObserveWorkflowsUseCase
import com.flowpulse.app.domain.usecase.ToggleWorkflowUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@HiltViewModel
class WorkflowsViewModel @Inject constructor(
    private val observeWorkflowsUseCase: ObserveWorkflowsUseCase,
    private val toggleWorkflowUseCase: ToggleWorkflowUseCase,
    private val executeWorkflowWebhookUseCase: ExecuteWorkflowWebhookUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(WorkflowsUiState())
    val state: StateFlow<WorkflowsUiState> = _state.asStateFlow()

    fun observe(instanceId: Long) {
        viewModelScope.launch {
            observeWorkflowsUseCase(instanceId).collectLatest { workflows ->
                _state.value = _state.value.copy(workflows = workflows)
            }
        }
        viewModelScope.launch {
            val result = observeWorkflowsUseCase.refresh(instanceId)
            if (result.isFailure) {
                _state.value = _state.value.copy(error = result.exceptionOrNull()?.message)
            }
        }
    }

    fun toggle(instanceId: Long, workflowId: Long, activate: Boolean) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            val result = toggleWorkflowUseCase(instanceId, workflowId, activate)
            _state.value = _state.value.copy(isLoading = false, error = result.exceptionOrNull()?.message)
        }
    }

    fun triggerWebhook(instanceId: Long, workflowId: Long) {
        viewModelScope.launch {
            executeWorkflowWebhookUseCase(instanceId, workflowId)
        }
    }
}

data class WorkflowsUiState(
    val workflows: List<Workflow> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
