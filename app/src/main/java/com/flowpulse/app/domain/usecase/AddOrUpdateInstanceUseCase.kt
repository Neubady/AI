package com.flowpulse.app.domain.usecase

import com.flowpulse.app.domain.model.N8nInstance
import com.flowpulse.app.domain.repo.FlowPulseRepository
import javax.inject.Inject

class AddOrUpdateInstanceUseCase @Inject constructor(
    private val repository: FlowPulseRepository
) {
    suspend operator fun invoke(instance: N8nInstance): Result<Unit> {
        val validation = repository.validateInstance(instance)
        if (validation.isSuccess) {
            repository.addOrUpdateInstance(instance)
        }
        return validation
    }
}
