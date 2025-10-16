package com.flowpulse.app.domain.usecase

import com.flowpulse.app.domain.repo.FlowPulseRepository
import javax.inject.Inject

class DeleteExecutionUseCase @Inject constructor(
    private val repository: FlowPulseRepository
) {
    suspend operator fun invoke(instanceId: Long, executionId: Long): Result<Unit> =
        repository.deleteExecution(instanceId, executionId)
}
