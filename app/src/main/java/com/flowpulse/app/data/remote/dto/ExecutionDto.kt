package com.flowpulse.app.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class ExecutionDto(
    @SerialName("id") val id: Long,
    @SerialName("workflowId") val workflowId: Long?,
    @SerialName("status") val status: String,
    @SerialName("startedAt") val startedAt: String?,
    @SerialName("stoppedAt") val stoppedAt: String?,
    @SerialName("error") val error: String? = null,
    @SerialName("data") val data: JsonElement? = null
)
