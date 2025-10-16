package com.flowpulse.app.ui.screens.actions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flowpulse.app.domain.model.QuickAction
import com.flowpulse.app.domain.usecase.ManageQuickActionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@HiltViewModel
class ActionsViewModel @Inject constructor(
    private val manageQuickActionUseCase: ManageQuickActionUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ActionsUiState())
    val state: StateFlow<ActionsUiState> = _state.asStateFlow()

    fun observe(instanceId: Long) {
        viewModelScope.launch {
            manageQuickActionUseCase.observe(instanceId).collectLatest { actions ->
                _state.value = _state.value.copy(actions = actions)
            }
        }
    }

    fun save(action: QuickAction) {
        viewModelScope.launch { manageQuickActionUseCase.upsert(action) }
    }

    fun delete(id: Long) {
        viewModelScope.launch { manageQuickActionUseCase.delete(id) }
    }
}

data class ActionsUiState(
    val actions: List<QuickAction> = emptyList()
)
