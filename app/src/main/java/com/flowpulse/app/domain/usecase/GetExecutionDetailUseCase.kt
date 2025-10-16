package com.flowpulse.app.domain.usecase

import com.flowpulse.app.domain.model.Execution
import com.flowpulse.app.domain.repo.FlowPulseRepository
import javax.inject.Inject

class GetExecutionDetailUseCase @Inject constructor(
    private val repository: FlowPulseRepository
) {
    suspend operator fun invoke(instanceId: Long, executionId: Long): Result<Execution> =
        repository.getExecution(instanceId, executionId)
}
