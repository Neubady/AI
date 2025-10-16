package com.flowpulse.app.domain.usecase

import com.flowpulse.app.domain.repo.FlowPulseRepository
import javax.inject.Inject

class DeleteInstanceUseCase @Inject constructor(
    private val repository: FlowPulseRepository
) {
    suspend operator fun invoke(id: Long) = repository.deleteInstance(id)
}
