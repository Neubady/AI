package com.flowpulse.app.domain.repo

import com.flowpulse.app.domain.model.Execution
import com.flowpulse.app.domain.model.InstanceMetrics
import com.flowpulse.app.domain.model.N8nInstance
import com.flowpulse.app.domain.model.QuickAction
import com.flowpulse.app.domain.model.Alert
import com.flowpulse.app.domain.model.Workflow
import kotlinx.coroutines.flow.Flow

interface FlowPulseRepository {
    fun observeInstances(): Flow<List<N8nInstance>>
    suspend fun getInstance(id: Long): N8nInstance?
    suspend fun addOrUpdateInstance(instance: N8nInstance)
    suspend fun deleteInstance(id: Long)
    suspend fun validateInstance(instance: N8nInstance): Result<Unit>

    suspend fun fetchMetrics(instanceId: Long): Result<InstanceMetrics>

    fun observeWorkflows(instanceId: Long): Flow<List<Workflow>>
    suspend fun refreshWorkflows(instanceId: Long): Result<Unit>
    suspend fun toggleWorkflow(instanceId: Long, workflowId: Long, activate: Boolean): Result<Unit>
    suspend fun executeWorkflowWebhook(instanceId: Long, workflowId: Long): Result<Unit>

    fun observeExecutions(instanceId: Long): Flow<List<Execution>>
    suspend fun refreshExecutions(instanceId: Long, status: String? = null): Result<Unit>
    suspend fun getExecution(instanceId: Long, executionId: Long): Result<Execution>
    suspend fun deleteExecution(instanceId: Long, executionId: Long): Result<Unit>

    fun observeQuickActions(instanceId: Long): Flow<List<QuickAction>>
    suspend fun upsertQuickAction(action: QuickAction)
    suspend fun deleteQuickAction(id: Long)

    fun observeAlerts(instanceId: Long): Flow<List<Alert>>
    suspend fun saveAlert(alert: Alert)
}
