package com.flowpulse.app.domain.usecase

import com.flowpulse.app.domain.model.Execution
import com.flowpulse.app.domain.repo.FlowPulseRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveExecutionsUseCase @Inject constructor(
    private val repository: FlowPulseRepository
) {
    operator fun invoke(instanceId: Long): Flow<List<Execution>> = repository.observeExecutions(instanceId)
    suspend fun refresh(instanceId: Long, status: String? = null): Result<Unit> = repository.refreshExecutions(instanceId, status)
}
