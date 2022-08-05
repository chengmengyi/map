package com.demo.map.admob

import com.demo.map.base.BaseActivity
import com.demo.map.ext.ActivityLifecycleCallback
import com.demo.map.ext.printMap
import com.google.android.gms.ads.appopen.AppOpenAd
import com.google.android.gms.ads.interstitial.InterstitialAd
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ShowFullAd(
    private val context:BaseActivity,
    private val type:String,
) {

    fun getHasAd() = null!=LoadAdManager.getAdByType(type)

    fun showFullAd(onNext:(() -> Unit)? = null){
        val adDataBean = LoadAdManager.getAdByType(type)
        if (null!=adDataBean){
            if (null==adDataBean.ad){
                onNext?.invoke()
            }else{
                if (LoadAdManager.isShowingFullAd||!checkCanShowFullAd()){
                    onNext?.invoke()
                    return
                }
                printMap("show $type ad ")
                when(adDataBean.ad){
                    is AppOpenAd-> showOpenAd(adDataBean.ad,onNext=onNext)
                    is InterstitialAd-> showInterstitialAdAd(adDataBean.ad,onNext=onNext)
                }
            }
        }
    }

    private fun showOpenAd(ad:AppOpenAd,onNext:(() -> Unit)? = null){
        LoadAdManager.isShowingFullAd=true
        ad.fullScreenContentCallback=FullAdShowCallback(type,next = { onNextCall(onNext) })
        ad.show(context)
    }

    private fun onNextCall(onNext:(() -> Unit)? = null){
        GlobalScope.launch(Dispatchers.Main) {
            delay(200L)
            if (context.getResume()){
                onNext?.invoke()
            }
        }
    }

    private fun showInterstitialAdAd(ad:InterstitialAd,onNext:(() -> Unit)? = null){
        LoadAdManager.isShowingFullAd=true
        ad.fullScreenContentCallback=FullAdShowCallback(type,next = { onNextCall(onNext) })
        ad.show(context)
    }

    private fun checkCanShowFullAd()=!LoadAdManager.isShowingFullAd&&context.getResume()
}