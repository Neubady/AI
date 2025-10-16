package com.flowpulse.app.ui.screens.instances

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flowpulse.app.BuildConfig
import com.flowpulse.app.domain.model.AuthType
import com.flowpulse.app.domain.model.N8nInstance
import com.flowpulse.app.domain.usecase.AddOrUpdateInstanceUseCase
import com.flowpulse.app.domain.usecase.DeleteInstanceUseCase
import com.flowpulse.app.domain.usecase.ObserveInstancesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@HiltViewModel
class InstancesViewModel @Inject constructor(
    private val observeInstances: ObserveInstancesUseCase,
    private val addOrUpdateInstance: AddOrUpdateInstanceUseCase,
    private val deleteInstance: DeleteInstanceUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(InstancesUiState())
    val state: StateFlow<InstancesUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            observeInstances().collectLatest { instances ->
                val limit = BuildConfig.FREE_INSTANCE_LIMIT
                _state.value = _state.value.copy(instances = instances, isLimitReached = instances.size >= limit)
            }
        }
    }

    fun onNameChange(value: String) {
        _state.value = _state.value.copy(form = _state.value.form.copy(name = value))
    }

    fun onUrlChange(value: String) {
        _state.value = _state.value.copy(form = _state.value.form.copy(baseUrl = value))
    }

    fun onTokenChange(value: String) {
        _state.value = _state.value.copy(form = _state.value.form.copy(token = value))
    }

    fun onAuthTypeSelected(type: AuthType) {
        _state.value = _state.value.copy(form = _state.value.form.copy(authType = type))
    }

    fun saveInstance() {
        val form = _state.value.form
        if (!form.isValid()) {
            _state.value = _state.value.copy(error = "Invalid form")
            return
        }
        viewModelScope.launch {
            _state.value = _state.value.copy(isSaving = true)
            val result = addOrUpdateInstance(
                N8nInstance(
                    id = form.id,
                    name = form.name,
                    baseUrl = form.baseUrl,
                    authType = form.authType,
                    apiKey = form.token,
                    username = form.username,
                    password = form.password,
                    developerMode = form.developerMode
                )
            )
            _state.value = if (result.isSuccess) {
                InstancesUiState(instances = _state.value.instances)
            } else {
                _state.value.copy(isSaving = false, error = result.exceptionOrNull()?.message)
            }
        }
    }

    fun editInstance(instance: N8nInstance) {
        _state.value = _state.value.copy(
            form = InstanceFormState(
                id = instance.id,
                name = instance.name,
                baseUrl = instance.baseUrl,
                token = instance.apiKey,
                authType = instance.authType,
                username = instance.username,
                password = instance.password,
                developerMode = instance.developerMode
            )
        )
    }

    fun deleteInstance(id: Long) {
        viewModelScope.launch {
            deleteInstance(id)
        }
    }

    fun resetForm() {
        _state.value = _state.value.copy(form = InstanceFormState())
    }

}

data class InstancesUiState(
    val instances: List<N8nInstance> = emptyList(),
    val form: InstanceFormState = InstanceFormState(),
    val isSaving: Boolean = false,
    val isLimitReached: Boolean = false,
    val error: String? = null
)

data class InstanceFormState(
    val id: Long = 0,
    val name: String = "",
    val baseUrl: String = "",
    val token: String = "",
    val authType: AuthType = AuthType.BEARER,
    val username: String? = null,
    val password: String? = null,
    val developerMode: Boolean = false
) {
    fun isValid(): Boolean = name.isNotBlank() && baseUrl.startsWith("http") && token.isNotBlank()
}
