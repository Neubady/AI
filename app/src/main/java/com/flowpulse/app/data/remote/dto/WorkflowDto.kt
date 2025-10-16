package com.flowpulse.app.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WorkflowDto(
    @SerialName("id") val id: Long,
    @SerialName("name") val name: String,
    @SerialName("active") val active: Boolean,
    @SerialName("updatedAt") val updatedAt: String?
)
