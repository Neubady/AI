package com.flowpulse.app.data.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WorkflowDto(
    val id: Long,
    val name: String,
    val active: Boolean,
    val updatedAt: String? = null
)

@Serializable
data class WorkflowsResponse(
    val data: List<WorkflowDto>
)

@Serializable
data class ToggleWorkflowRequest(
    @SerialName("activate") val activate: Boolean
)

@Serializable
data class ExecutionDto(
    val id: Long,
    val workflowId: Long? = null,
    val mode: String? = null,
    val status: String? = null,
    val startedAt: String? = null,
    val stoppedAt: String? = null,
    val finished: Boolean? = null,
    val error: ExecutionErrorDto? = null,
    val data: Map<String, Any?>? = null
)

@Serializable
data class ExecutionErrorDto(
    val message: String? = null,
    val stack: String? = null
)

@Serializable
data class ExecutionsResponse(
    val data: List<ExecutionDto>,
    val nextCursor: Long? = null
)

@Serializable
data class HealthDto(
    val status: String,
    val version: String? = null,
    val message: String? = null
)
