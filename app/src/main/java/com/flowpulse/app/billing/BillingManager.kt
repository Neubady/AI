package com.flowpulse.app.billing

import android.app.Activity
import android.content.Context
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.QueryProductDetailsParams
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

@Singleton
class BillingManager @Inject constructor(
    @ApplicationContext private val context: Context
) : PurchasesUpdatedListener {

    private val billingClient: BillingClient = BillingClient.newBuilder(context)
        .setListener(this)
        .enablePendingPurchases()
        .build()

    private val _purchases = MutableSharedFlow<List<Purchase>>(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    val purchases: SharedFlow<List<Purchase>> = _purchases

    fun connect(onReady: () -> Unit = {}) {
        if (billingClient.isReady) {
            onReady()
            return
        }
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(result: BillingResult) {
                if (result.responseCode == BillingClient.BillingResponseCode.OK) {
                    onReady()
                }
            }

            override fun onBillingServiceDisconnected() {
                billingClient.startConnection(this)
            }
        })
    }

    fun launchBillingFlow(activity: Activity, productDetails: ProductDetails) {
        val offerToken = productDetails.subscriptionOfferDetails?.firstOrNull()?.offerToken
        val params = BillingFlowParams.newBuilder()
            .setProductDetailsParamsList(
                listOf(
                    BillingFlowParams.ProductDetailsParams.newBuilder()
                        .setProductDetails(productDetails)
                        .setOfferToken(offerToken ?: "")
                        .build()
                )
            )
            .build()
        billingClient.launchBillingFlow(activity, params)
    }

    suspend fun queryProducts(): List<ProductDetails> {
        val params = QueryProductDetailsParams.newBuilder()
            .setProductList(
                listOf(
                    QueryProductDetailsParams.Product.newBuilder()
                        .setProductId(System.getenv("BILLING_MONTHLY_PRODUCT_ID") ?: "flowpulse.pro.monthly")
                        .setProductType(BillingClient.ProductType.SUBS)
                        .build(),
                    QueryProductDetailsParams.Product.newBuilder()
                        .setProductId(System.getenv("BILLING_LIFETIME_PRODUCT_ID") ?: "flowpulse.pro.lifetime")
                        .setProductType(BillingClient.ProductType.INAPP)
                        .build()
                )
            ).build()
        val result = billingClient.queryProductDetails(params)
        return result.productDetailsList ?: emptyList()
    }

    override fun onPurchasesUpdated(result: BillingResult, purchases: MutableList<Purchase>?) {
        if (result.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
            _purchases.tryEmit(purchases)
        }
    }
}
