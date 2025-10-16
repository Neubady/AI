package com.flowpulse.app.domain.usecase

import com.flowpulse.app.domain.model.QuickAction
import com.flowpulse.app.domain.repo.FlowPulseRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ManageQuickActionUseCase @Inject constructor(
    private val repository: FlowPulseRepository
) {
    fun observe(instanceId: Long): Flow<List<QuickAction>> = repository.observeQuickActions(instanceId)
    suspend fun upsert(action: QuickAction) = repository.upsertQuickAction(action)
    suspend fun delete(id: Long) = repository.deleteQuickAction(id)
}
