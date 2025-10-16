package com.flowpulse.app.domain.usecase

import com.flowpulse.app.domain.repo.FlowPulseRepository
import javax.inject.Inject

class ToggleWorkflowUseCase @Inject constructor(
    private val repository: FlowPulseRepository
) {
    suspend operator fun invoke(instanceId: Long, workflowId: Long, activate: Boolean): Result<Unit> {
        return repository.toggleWorkflow(instanceId, workflowId, activate)
    }
}
