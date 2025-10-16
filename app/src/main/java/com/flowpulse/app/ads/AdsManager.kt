package com.flowpulse.app.ads

import android.app.Activity
import android.content.Context
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AdsManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private var interstitialAd: InterstitialAd? = null

    init {
        MobileAds.initialize(context)
    }

    fun loadInterstitial(adUnitId: String, onLoaded: () -> Unit = {}) {
        val request = AdRequest.Builder().build()
        InterstitialAd.load(
            context = context,
            adUnitId = adUnitId,
            adRequest = request,
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: InterstitialAd) {
                    interstitialAd = ad
                    onLoaded()
                }
            }
        )
    }

    fun showInterstitial(activity: Activity, onDismissed: () -> Unit = {}) {
        interstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                onDismissed()
                interstitialAd = null
            }

            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                interstitialAd = null
                onDismissed()
            }
        }
        interstitialAd?.show(activity) ?: onDismissed()
    }
}
