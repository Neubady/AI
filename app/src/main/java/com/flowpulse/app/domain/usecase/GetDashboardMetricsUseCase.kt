package com.flowpulse.app.domain.usecase

import com.flowpulse.app.domain.model.InstanceMetrics
import com.flowpulse.app.domain.repo.FlowPulseRepository
import javax.inject.Inject

class GetDashboardMetricsUseCase @Inject constructor(
    private val repository: FlowPulseRepository
) {
    suspend operator fun invoke(instanceId: Long): Result<InstanceMetrics> {
        return repository.fetchMetrics(instanceId)
    }
}
