package com.flowpulse.app.domain.usecase

import android.net.Uri
import com.flowpulse.app.domain.model.DeepLinkResult
import javax.inject.Inject

class ProcessDeepLinkUseCase @Inject constructor() {
    operator fun invoke(uri: Uri): DeepLinkResult? {
        val executionId = uri.getQueryParameter("executionId")?.toLongOrNull()
        val instanceId = uri.getQueryParameter("instanceId")?.toLongOrNull()
        return if (instanceId != null) {
            DeepLinkResult(
                instanceId = instanceId,
                executionId = executionId,
                destination = uri.getQueryParameter("destination") ?: "executions/$instanceId"
            )
        } else {
            null
        }
    }
}
