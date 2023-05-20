package com.mussayev.sudoku.domain.manager

import android.app.Activity
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.admanager.AdManagerAdView

interface AdManager {
    fun showBannerAd(adView: AdManagerAdView)
    fun loadInterstitialAd(onAdLoaded: () -> Unit, onAdFailedToLoad: (LoadAdError) -> Unit)
    fun showInterstitialAd(activity: Activity, onAdClosed: () -> Unit)
    fun getAdRequest(): AdRequest
    fun wasAdShown(): Boolean
}
