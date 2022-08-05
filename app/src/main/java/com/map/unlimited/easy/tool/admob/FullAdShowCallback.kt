package com.map.unlimited.easy.tool.admob

import com.map.unlimited.easy.tool.ext.printMap
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.FullScreenContentCallback

class FullAdShowCallback(
    private val type:String,
    private val next:()->Unit
) :FullScreenContentCallback() {
    override fun onAdDismissedFullScreenContent() {
        super.onAdDismissedFullScreenContent()
        LoadAdManager.isShowingFullAd=false
        if (type==AdType.TYPE_CONNECT){
            LoadAdManager.loadAd(type)
        }
        next()
    }

    override fun onAdShowedFullScreenContent() {
        super.onAdShowedFullScreenContent()
        LoadAdManager.isShowingFullAd=true
        LoadAdManager.clearAdCache(type)
    }

    override fun onAdFailedToShowFullScreenContent(p0: AdError) {
        super.onAdFailedToShowFullScreenContent(p0)
        LoadAdManager.isShowingFullAd=false
        LoadAdManager.clearAdCache(type)
        next()
    }
}