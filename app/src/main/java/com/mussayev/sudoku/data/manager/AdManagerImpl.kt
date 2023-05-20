package com.mussayev.sudoku.data.manager

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.admanager.AdManagerAdView
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.mussayev.sudoku.R
import com.mussayev.sudoku.domain.manager.AdManager

class AdManagerImpl(private val context: Context) : AdManager {
    private val adRequest = AdRequest.Builder().build()
    private var interstitialAd: InterstitialAd? = null

    private var adShown = false

    private var lastAdShownTime: Long = 0

//    init {
//        loadInterstitialAd({}, { _ -> })
//    }

    override fun getAdRequest(): AdRequest {
        return adRequest
    }

    override fun showBannerAd(adView: AdManagerAdView) {
        adView.loadAd(adRequest)
    }

    override fun loadInterstitialAd(onAdLoaded: () -> Unit, onAdFailedToLoad: (LoadAdError) -> Unit) {
        //val adUnitId = "ca-app-pub-3940256099942544/1033173712"
        val adUnitId = context.getString(R.string.ID_Interstitial) // Идентификатор межстраничного блока
        InterstitialAd.load(context, adUnitId, adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                this@AdManagerImpl.interstitialAd = interstitialAd
                onAdLoaded()
            }

            override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                onAdFailedToLoad(loadAdError)
            }
        }
        )
    }

    override fun showInterstitialAd(activity: Activity, onAdClosed: () -> Unit) {
        val currentTime = System.currentTimeMillis()
        val elapsedSinceLastAd = currentTime - lastAdShownTime
        if (interstitialAd != null) { // && elapsedSinceLastAd > MIN_TIME_BETWEEN_ADS
            interstitialAd?.let { ad ->
                ad.fullScreenContentCallback = object : FullScreenContentCallback() {
                    override fun onAdDismissedFullScreenContent() {
                        adShown = true // обновляем переменную при закрытии рекламы
                        lastAdShownTime = System.currentTimeMillis() // Сохраняем время последнего показа
                        onAdClosed()
                        loadInterstitialAd(
                            onAdLoaded = {},
                            onAdFailedToLoad = { _ -> }
                        ) // Перезагрузить межстраничный блок после закрытия
                    }
                }
                ad.show(activity)
            }
        } else {
            onAdClosed() // Вызывать колбэк onAdClosed, если межстраничный блок равен null или не прошло достаточно времени с момента последнего показа
        }
    }

    // Добавьте эту функцию для проверки, была ли показана реклама
    override fun wasAdShown(): Boolean {
        return adShown && isConnectedToInternet()
    }

    private fun isConnectedToInternet(): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork
        val networkCapabilities = connectivityManager.getNetworkCapabilities(network)

        return networkCapabilities != null &&
                (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                        networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                        networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET))
    }

    companion object {
        private const val MIN_TIME_BETWEEN_ADS = 60_000 // Минимальное время (в миллисекундах) между показами межстраничных блоков
    }
}
