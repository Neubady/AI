package com.flowpulse.app.domain.usecase

import com.flowpulse.app.domain.model.N8nInstance
import com.flowpulse.app.domain.repo.FlowPulseRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveInstancesUseCase @Inject constructor(
    private val repository: FlowPulseRepository
) {
    operator fun invoke(): Flow<List<N8nInstance>> = repository.observeInstances()
}
