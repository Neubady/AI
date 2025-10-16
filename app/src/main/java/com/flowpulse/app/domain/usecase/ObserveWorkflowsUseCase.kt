package com.flowpulse.app.domain.usecase

import com.flowpulse.app.domain.model.Workflow
import com.flowpulse.app.domain.repo.FlowPulseRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveWorkflowsUseCase @Inject constructor(
    private val repository: FlowPulseRepository
) {
    operator fun invoke(instanceId: Long): Flow<List<Workflow>> = repository.observeWorkflows(instanceId)
    suspend fun refresh(instanceId: Long): Result<Unit> = repository.refreshWorkflows(instanceId)
}
