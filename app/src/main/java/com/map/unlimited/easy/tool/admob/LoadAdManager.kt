package com.map.unlimited.easy.tool.admob

import com.map.unlimited.easy.tool.aMap
import com.map.unlimited.easy.tool.ext.printMap
import com.map.unlimited.easy.tool.interfaces.ILoadAdResultInterface
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.appopen.AppOpenAd
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.nativead.NativeAdOptions
import org.json.JSONObject
import org.slf4j.helpers.Util
import java.lang.Exception

object LoadAdManager {
    var isShowingFullAd=false
    private val isLoading= arrayListOf<String>()
    private val cacheAd= hashMapOf<String,AdDataBean>()

    fun loadAd(type:String){
        if (isLoading.contains(type)){
            printMap("isLoading ${type} ad")
            return
        }

        if (cacheAd.containsKey(type)){
            val adDataBean = cacheAd[type]
            if (null!=adDataBean?.ad){
                if (adDataBean.isExpired()){
                    printMap("ad is expired,${type}")
                    clearAdCache(type)
                }else{
                    printMap("$type ad has cache")
                    return
                }
            }
        }

        val adList = initAdList(type)
        if (adList.isEmpty()){
            printMap("ad is empty,${type}")
            return
        }

        isLoading.add(type)
        val callback=object :ILoadAdResultInterface{
            override fun loadAdResult(adDataBean: AdDataBean) {
                isLoading.remove(type)
                cacheAd[type]=adDataBean
            }
        }
        if (type==AdType.TYPE_OPEN){
            var loadNum=0
            val result=object : ILoadAdResultInterface{
                override fun loadAdResult(adDataBean: AdDataBean) {
                    if (null==adDataBean.ad&&loadNum==0){
                        loadNum++
                        preLoad(type,adList,iLoadAdCallback = callback)
                    }else{
                        callback.loadAdResult(adDataBean)
                    }
                }
            }
            preLoad(type,adList,iLoadAdCallback = result)
        }else{
            preLoad(type,adList,iLoadAdCallback = callback)
        }
    }

    private fun preLoad(type: String,adList:List<ConfAdBean>,index:Int = 0,iLoadAdCallback: ILoadAdResultInterface){
        if (index>=adList.size){
            iLoadAdCallback.loadAdResult(AdDataBean())
            return
        }
        val result=object : ILoadAdResultInterface{
            override fun loadAdResult(adDataBean: AdDataBean) {
                if (null==adDataBean.ad){
                    preLoad(type,adList, index=index+1,iLoadAdCallback = iLoadAdCallback)
                }else{
                    iLoadAdCallback.loadAdResult(adDataBean)
                }
            }
        }
        loadAdmob(type,adList[index],result)
    }

    private fun loadAdmob(type: String,confAdBean: ConfAdBean,iLoadAdCallback: ILoadAdResultInterface){
        printMap("start load $type ad,params:${confAdBean.toString()}")
        when(confAdBean.map_type){
            "kp"-> {
                AppOpenAd.load(
                    aMap,
                    confAdBean.map_id,
                    AdRequest.Builder().build(),
                    AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT,
                    object : AppOpenAd.AppOpenAdLoadCallback(){
                        override fun onAdLoaded(p0: AppOpenAd) {
                            super.onAdLoaded(p0)
                            printMap("load $type success")
                            iLoadAdCallback.loadAdResult(AdDataBean(ad = p0,loadTime = System.currentTimeMillis()))
                        }

                        override fun onAdFailedToLoad(p0: LoadAdError) {
                            super.onAdFailedToLoad(p0)
                            printMap("load $type fail ,${p0.message}")
                            iLoadAdCallback.loadAdResult(AdDataBean())
                        }
                    }
                )
            }
            "cp"-> {
                InterstitialAd.load(
                    aMap,
                    confAdBean.map_id,
                    AdRequest.Builder().build(),
                    object : InterstitialAdLoadCallback(){
                        override fun onAdFailedToLoad(p0: LoadAdError) {
                            printMap("load $type fail ,${p0.message}")
                            iLoadAdCallback.loadAdResult(AdDataBean())
                        }

                        override fun onAdLoaded(p0: InterstitialAd) {
                            printMap("load $type success")
                            iLoadAdCallback.loadAdResult(AdDataBean(ad = p0,loadTime = System.currentTimeMillis()))
                        }
                    }
                )
            }
            "ys"-> {
                AdLoader.Builder(
                    aMap,
                    confAdBean.map_id
                ).forNativeAd {
                    printMap("load $type success")
                    iLoadAdCallback.loadAdResult(AdDataBean(ad = it,loadTime = System.currentTimeMillis()))
                    }
                    .withAdListener(object : AdListener(){
                        override fun onAdFailedToLoad(p0: LoadAdError) {
                            super.onAdFailedToLoad(p0)
                            printMap("load $type fail ,${p0.message}")
                            iLoadAdCallback.loadAdResult(AdDataBean())
                        }
                    })
                    .withNativeAdOptions(
                        NativeAdOptions.Builder()
                            .setAdChoicesPlacement(
                                if (type==AdType.TYPE_HOME) NativeAdOptions.ADCHOICES_TOP_RIGHT
                                else NativeAdOptions.ADCHOICES_TOP_LEFT
                            )
                            .build()
                    )
                    .build()
                    .loadAd(AdRequest.Builder().build())
            }
        }
    }

    private fun initAdList(type: String):List<ConfAdBean>{
        val list= arrayListOf<ConfAdBean>()
        try {
            val jsonArray = JSONObject(ReadConfManager.getAdConf()).getJSONArray(type)
            for (index in 0 until jsonArray.length()){
                val jsonObject = jsonArray.getJSONObject(index)
                list.add(
                    ConfAdBean(
                        jsonObject.optString("map_source"),
                        jsonObject.optString("map_id"),
                        jsonObject.optString("map_type"),
                        jsonObject.optInt("map_sort"),
                    )
                )
            }
        }catch (e:Exception){

        }
        return list.filter { it.map_source == "admob" }.sortedByDescending { it.map_sort }
    }

    fun getAdByType(type: String) = cacheAd[type]

    fun clearAdCache(type:String){
        cacheAd.remove(type)
    }
}