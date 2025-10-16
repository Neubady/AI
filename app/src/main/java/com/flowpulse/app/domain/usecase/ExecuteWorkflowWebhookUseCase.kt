package com.flowpulse.app.domain.usecase

import com.flowpulse.app.domain.repo.FlowPulseRepository
import javax.inject.Inject

class ExecuteWorkflowWebhookUseCase @Inject constructor(
    private val repository: FlowPulseRepository
) {
    suspend operator fun invoke(instanceId: Long, workflowId: Long): Result<Unit> =
        repository.executeWorkflowWebhook(instanceId, workflowId)
}
