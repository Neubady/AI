package com.flowpulse.app.data.repo.impl

import android.content.Context
import com.flowpulse.app.BuildConfig
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesUpdatedListener
import com.flowpulse.app.domain.repo.BillingRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BillingRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : BillingRepository, PurchasesUpdatedListener {

    private val billingClient: BillingClient = BillingClient.newBuilder(context)
        .enablePendingPurchases()
        .setListener(this)
        .build()

    private val _isProUser = MutableStateFlow(false)
    override val isProUser: Flow<Boolean> = _isProUser.asStateFlow()

    private val purchaseEvents = Channel<List<Purchase>>(Channel.BUFFERED)

    init {
        connect()
    }

    private fun connect() {
        if (billingClient.isReady) return
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingServiceDisconnected() {
                // Retry handled on demand
            }

            override fun onBillingSetupFinished(result: com.android.billingclient.api.BillingResult) {
                if (result.responseCode == BillingClient.BillingResponseCode.OK) {
                    // TODO: Query purchases and update state
                }
            }
        })
    }

    override suspend fun launchPurchase(productId: String) {
        // Documented en README: el flujo real se lanza desde Activity usando BillingClient.launchBillingFlow
    }

    override suspend fun restorePurchases() {
        connect()
        // TODO: Query purchases and update state
    }

    override fun onPurchasesUpdated(
        billingResult: com.android.billingclient.api.BillingResult,
        purchases: MutableList<Purchase>?
    ) {
        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
            purchaseEvents.trySend(purchases)
            _isProUser.value = purchases.any { it.products.contains(BuildConfig.BILLING_MONTHLY_ID) || it.products.contains(BuildConfig.BILLING_LIFETIME_ID) }
        }
    }
}
