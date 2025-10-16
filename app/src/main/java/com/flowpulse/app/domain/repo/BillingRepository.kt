package com.flowpulse.app.domain.repo

import kotlinx.coroutines.flow.Flow

interface BillingRepository {
    val isProUser: Flow<Boolean>
    suspend fun launchPurchase(productId: String)
    suspend fun restorePurchases()
}
