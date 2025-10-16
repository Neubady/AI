package com.flowpulse.app.domain.model

data class DeepLinkResult(
    val instanceId: Long,
    val executionId: Long?,
    val destination: String
)
