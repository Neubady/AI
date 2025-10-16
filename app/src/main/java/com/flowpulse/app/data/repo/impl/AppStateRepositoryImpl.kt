package com.flowpulse.app.data.repo.impl

import com.flowpulse.app.domain.model.FlowPulseAppState
import com.flowpulse.app.domain.repo.AppStateRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppStateRepositoryImpl @Inject constructor() : AppStateRepository {

    private val state = MutableStateFlow<FlowPulseAppState>(FlowPulseAppState.Loading)

    override fun observeState(): Flow<FlowPulseAppState> = state.asStateFlow()

    override suspend fun updateLockState(locked: Boolean) {
        val current = state.value
        state.value = if (locked) {
            FlowPulseAppState.Locked()
        } else {
            when (current) {
                is FlowPulseAppState.Ready -> current
                else -> FlowPulseAppState.Ready(hasPro = false, instances = 0)
            }
        }
    }
}
