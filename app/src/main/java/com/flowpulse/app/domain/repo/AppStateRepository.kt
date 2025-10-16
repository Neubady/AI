package com.flowpulse.app.domain.repo

import com.flowpulse.app.domain.model.FlowPulseAppState
import kotlinx.coroutines.flow.Flow

interface AppStateRepository {
    fun observeState(): Flow<FlowPulseAppState>
    suspend fun updateLockState(locked: Boolean)
}
