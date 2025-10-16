package com.flowpulse.app.domain.usecase

import com.flowpulse.app.domain.model.FlowPulseAppState
import com.flowpulse.app.domain.repo.AppStateRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveAppStateUseCase @Inject constructor(
    private val repository: AppStateRepository
) {
    operator fun invoke(): Flow<FlowPulseAppState> = repository.observeState()
}
