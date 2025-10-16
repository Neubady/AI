package com.flowpulse.app.data.repo

import android.util.Base64
import com.flowpulse.app.BuildConfig
import com.flowpulse.app.data.local.AppPreferences
import com.flowpulse.app.data.local.dao.AlertDao
import com.flowpulse.app.data.local.dao.ExecutionDao
import com.flowpulse.app.data.local.dao.InstanceDao
import com.flowpulse.app.data.local.dao.QuickActionDao
import com.flowpulse.app.data.local.dao.WorkflowDao
import com.flowpulse.app.data.remote.N8nClientFactory
import com.flowpulse.app.data.remote.ToggleWorkflowRequest
import com.flowpulse.app.domain.model.AuthType
import com.flowpulse.app.domain.model.Execution
import com.flowpulse.app.domain.model.InstanceMetrics
import com.flowpulse.app.domain.model.N8nInstance
import com.flowpulse.app.domain.model.Alert
import com.flowpulse.app.domain.model.QuickAction
import com.flowpulse.app.domain.model.Workflow
import com.flowpulse.app.domain.repo.FlowPulseRepository
import com.flowpulse.app.security.SecureStorage
import com.flowpulse.app.util.toDomain
import com.flowpulse.app.util.toEntity
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.atStartOfDayIn
import okhttp3.CertificatePinner
import okhttp3.Interceptor
import okhttp3.Response

@Singleton
class FlowPulseRepositoryImpl @Inject constructor(
    private val instanceDao: InstanceDao,
    private val workflowDao: WorkflowDao,
    private val executionDao: ExecutionDao,
    private val alertDao: AlertDao,
    private val quickActionDao: QuickActionDao,
    private val secureStorage: SecureStorage,
    private val clientFactory: N8nClientFactory,
    private val certificatePinner: CertificatePinner?,
    private val appPreferences: AppPreferences,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : FlowPulseRepository {

    override fun observeInstances(): Flow<List<N8nInstance>> {
        return instanceDao.observeInstances().map { entities ->
            entities.mapNotNull { entity ->
                val token = secureStorage.getSecret(tokenKey(entity.id)) ?: return@mapNotNull null
                entity.toDomain(token)
            }
        }
    }

    override suspend fun getInstance(id: Long): N8nInstance? = withContext(dispatcher) {
        val entity = instanceDao.getById(id) ?: return@withContext null
        val token = secureStorage.getSecret(tokenKey(id)) ?: return@withContext null
        entity.toDomain(token)
    }

    override suspend fun addOrUpdateInstance(instance: N8nInstance) = withContext(dispatcher) {
        if (instance.id == 0L) {
            val count = instanceDao.countInstances()
            if (!hasPro() && count >= BuildConfig.FREE_INSTANCE_LIMIT) {
                throw IllegalStateException("Instance limit reached for free tier")
            }
        }
        val encryptedToken = instance.apiKey
        val encryptedPassword = instance.password
        val entity = instance.toEntity(encryptedToken, encryptedPassword)
        val newId = instanceDao.upsert(entity)
        val id = if (instance.id == 0L) newId else instance.id
        secureStorage.putSecret(tokenKey(id), instance.apiKey)
        instance.username?.let { username ->
            instance.password?.let { secureStorage.putSecret(passwordKey(id), it) }
        }
    }

    override suspend fun deleteInstance(id: Long) = withContext(dispatcher) {
        instanceDao.delete(id)
        secureStorage.deleteSecret(tokenKey(id))
        secureStorage.deleteSecret(passwordKey(id))
    }

    override suspend fun validateInstance(instance: N8nInstance): Result<Unit> = runCatching {
        withContext(dispatcher) {
            val service = createService(instance)
            service.getWorkflows()
        }
    }

    override suspend fun fetchMetrics(instanceId: Long): Result<InstanceMetrics> = runCatching {
        withContext(dispatcher) {
            val workflows = workflowDao.getOnce(instanceId)
            val executions = executionDao.getByInstance(instanceId)
            val tz = TimeZone.currentSystemDefault()
            val now = Clock.System.now()
            val today = now.toLocalDateTime(tz).date
            val executionsToday = executions.count { entity ->
                entity.startedAt?.let { Instant.fromEpochMilliseconds(it).toLocalDateTime(tz).date == today } == true
            }
            val errorsToday = executions.count { entity ->
                entity.status.equals("error", true) && entity.startedAt?.let { Instant.fromEpochMilliseconds(it).toLocalDateTime(tz).date == today } == true
            }
            val latencies = executions.mapNotNull { entity ->
                if (entity.startedAt != null && entity.finishedAt != null) {
                    entity.finishedAt - entity.startedAt
                } else null
            }
            val averageLatency = if (latencies.isNotEmpty()) latencies.sum() / latencies.size else 0L
            val grouped = executions.groupBy { entity ->
                entity.startedAt?.let { Instant.fromEpochMilliseconds(it).toLocalDateTime(tz).date } ?: today
            }
            val chartPoints = grouped.entries.sortedBy { it.key }
                .takeLast(7)
                .map { entry ->
                    com.flowpulse.app.domain.model.MetricsPoint(
                        timestamp = entry.key.atStartOfDayIn(tz),
                        value = entry.value.size
                    )
                }
            InstanceMetrics(
                activeWorkflows = workflows.count { it.active },
                executionsToday = executionsToday,
                errorsToday = errorsToday,
                averageLatencyMs = averageLatency,
                chartPoints = chartPoints
            )
        }
    }

    override fun observeWorkflows(instanceId: Long): Flow<List<Workflow>> {
        return workflowDao.observe(instanceId).map { list -> list.map { it.toDomain() } }
    }

    override suspend fun refreshWorkflows(instanceId: Long): Result<Unit> = runCatching {
        withContext(dispatcher) {
            val instance = getInstance(instanceId) ?: throw IllegalStateException("Instance not found")
            val service = createService(instance)
            val response = service.getWorkflows()
            val entities = response.data.map { it.toEntity(instanceId) }
            workflowDao.upsertAll(entities)
        }
    }

    override suspend fun toggleWorkflow(instanceId: Long, workflowId: Long, activate: Boolean): Result<Unit> = runCatching {
        withContext(dispatcher) {
            val instance = getInstance(instanceId) ?: throw IllegalStateException("Instance not found")
            val service = createService(instance)
            service.toggleWorkflow(workflowId, ToggleWorkflowRequest(activate))
            workflowDao.updateActive(instanceId, workflowId, activate)
        }
    }

    override suspend fun executeWorkflowWebhook(instanceId: Long, workflowId: Long): Result<Unit> = runCatching {
        // Future implementation will trigger webhook execution.
    }

    override fun observeExecutions(instanceId: Long): Flow<List<Execution>> {
        return executionDao.observe(instanceId).map { list -> list.map { it.toDomain() } }
    }

    override suspend fun refreshExecutions(instanceId: Long, status: String?): Result<Unit> = runCatching {
        withContext(dispatcher) {
            val instance = getInstance(instanceId) ?: throw IllegalStateException("Instance not found")
            val service = createService(instance)
            val response = service.getExecutions(status = status)
            val entities = response.data.map { it.toEntity(instanceId) }
            executionDao.upsertAll(entities)
            if (status == "error") {
                val lastId = entities.maxOfOrNull { it.id } ?: 0L
                if (lastId > 0) {
                    appPreferences.setLastErrorExecutionId(instanceId, lastId)
                }
            }
        }
    }

    override suspend fun getExecution(instanceId: Long, executionId: Long): Result<Execution> = runCatching {
        withContext(dispatcher) {
            val local = executionDao.getById(executionId)
            if (local != null) {
                return@withContext local.toDomain()
            }
            val instance = getInstance(instanceId) ?: throw IllegalStateException("Instance not found")
            val service = createService(instance)
            service.getExecution(executionId).toEntity(instanceId).toDomain()
        }
    }

    override suspend fun deleteExecution(instanceId: Long, executionId: Long): Result<Unit> = runCatching {
        withContext(dispatcher) {
            val instance = getInstance(instanceId) ?: throw IllegalStateException("Instance not found")
            val service = createService(instance)
            service.deleteExecution(executionId)
            executionDao.delete(executionId)
        }
    }

    override fun observeQuickActions(instanceId: Long): Flow<List<QuickAction>> {
        return quickActionDao.observe(instanceId).map { list -> list.map { it.toDomain() } }
    }

    override suspend fun upsertQuickAction(action: QuickAction) = withContext(dispatcher) {
        quickActionDao.upsert(action.toEntity())
    }

    override suspend fun deleteQuickAction(id: Long) = withContext(dispatcher) {
        quickActionDao.delete(id)
    }

    override fun observeAlerts(instanceId: Long) = alertDao.observe(instanceId).map { list ->
        list.map { it.toDomain() }
    }

    override suspend fun saveAlert(alert: Alert) = withContext(dispatcher) {
        alertDao.insert(alert.toEntity())
    }

    private fun tokenKey(id: Long) = "token_$id"
    private fun passwordKey(id: Long) = "password_$id"

    private suspend fun createService(instance: N8nInstance) = withContext(dispatcher) {
        val baseUrl = ensureHttps(instance.baseUrl)
        val authHeader = when (instance.authType) {
            AuthType.BEARER -> "Bearer ${instance.apiKey}"
            AuthType.BASIC -> {
                val credentials = "${instance.username}:${instance.password ?: secureStorage.getSecret(passwordKey(instance.id))}".trimEnd(':')
                val encoded = Base64.encodeToString(credentials.toByteArray(), Base64.NO_WRAP)
                "Basic $encoded"
            }
        }
        val authInterceptor = Interceptor { chain ->
            val original = chain.request()
            val request = original.newBuilder()
                .header("Authorization", authHeader)
                .header("User-Agent", "FlowPulse/${BuildConfig.VERSION_NAME}")
                .build()
            var response: Response = chain.proceed(request)
            if (response.code in 500..599) {
                response.close()
                response = chain.proceed(request)
            }
            response
        }
        clientFactory.create(baseUrl, authInterceptor, certificatePinner)
    }

    private fun ensureHttps(url: String): String {
        var normalized = url.trim()
        if (!normalized.startsWith("http")) {
            normalized = "https://$normalized"
        }
        if (!normalized.endsWith('/')) {
            normalized += "/"
        }
        return normalized
    }

    private fun hasPro(): Boolean {
        // Placeholder: in production, verify entitlement from billing storage.
        return false
    }
}
