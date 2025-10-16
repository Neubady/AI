package com.flowpulse.app.ui.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flowpulse.app.domain.usecase.ObserveAppStateUseCase
import com.flowpulse.app.domain.usecase.ProcessDeepLinkUseCase
import com.flowpulse.app.ui.viewmodel.model.MainAction
import com.flowpulse.app.ui.viewmodel.model.MainState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val observeAppStateUseCase: ObserveAppStateUseCase,
    private val processDeepLinkUseCase: ProcessDeepLinkUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(MainState())
    val state: StateFlow<MainState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            observeAppStateUseCase().collect { appState ->
                _state.value = _state.value.copy(appState = appState)
            }
        }
    }

    fun onAction(action: MainAction) {
        when (action) {
            is MainAction.RequestBiometricUnlock -> {
                _state.value = _state.value.copy(requireAuthentication = action.required)
            }
            is MainAction.UpdateDestination -> {
                _state.value = _state.value.copy(currentDestination = action.route)
            }
        }
    }

    fun onStart(data: Uri?) {
        viewModelScope.launch {
            data?.let { processDeepLinkUseCase(it) }?.let { deepLinkResult ->
                _state.value = _state.value.copy(pendingDeepLink = deepLinkResult)
            }
        }
    }
}
