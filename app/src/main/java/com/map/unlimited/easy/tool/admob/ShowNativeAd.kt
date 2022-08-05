package com.map.unlimited.easy.tool.admob

import android.graphics.Outline
import android.view.View
import android.view.ViewOutlineProvider
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.utils.widget.ImageFilterView
import com.blankj.utilcode.util.SizeUtils
import com.map.unlimited.easy.tool.R
import com.map.unlimited.easy.tool.base.BaseActivity
import com.map.unlimited.easy.tool.ext.printMap
import com.map.unlimited.easy.tool.ext.show
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView
import kotlinx.coroutines.*

class ShowNativeAd(
    private val context:BaseActivity,
    private val type:String
) {
    private var nativeAd:NativeAd?=null
    private var loopJob:Job?=null


    fun loopGetNativeAd(){
        LoadAdManager.loadAd(type)
        loopJob=GlobalScope.launch(Dispatchers.Main) {
            delay(300L)
            if (context.getResume()){
                while (true){
                    if (!isActive) {
                        break
                    }
                    delay(1000L)
                    val adBean = LoadAdManager.getAdByType(type)
                    if (null!=adBean){
                        cancel()
                        val ad = adBean.ad
                        if (null!=ad&&ad is NativeAd){
                            destroyNativeAd()
                            nativeAd=ad
                            showNativeAd()
                        }
                    }
                }
            }
        }
    }

    private fun showNativeAd(){
        nativeAd?.let {
            printMap("show $type ad ")
            val nativeAdView=context.findViewById<NativeAdView>(R.id.nv_root_view)
            nativeAdView.mediaView=context.findViewById(R.id.mv_native_cover)
            nativeAdView.mediaView?.run {
                if (null!=it.mediaContent){
                    setMediaContent(it.mediaContent)
                    setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                }
                outlineProvider = object : ViewOutlineProvider() {
                    override fun getOutline(view: View?, outline: Outline?) {
                        if (view == null || outline == null) return
                        outline.setRoundRect(
                            0,
                            0,
                            view.width,
                            view.height,
                            SizeUtils.dp2px(10F).toFloat()
                        )
                        view.clipToOutline = true
                    }
                }
            }
            nativeAdView.headlineView=context.findViewById(R.id.tv_native_title)
            (nativeAdView.headlineView as AppCompatTextView).text=it.headline

            nativeAdView.bodyView=context.findViewById(R.id.tv_native_desc)
            (nativeAdView.bodyView as AppCompatTextView).text=it.body

            nativeAdView.iconView=context.findViewById(R.id.iv_native_logo)
            (nativeAdView.iconView as ImageFilterView).setImageDrawable(it.icon?.drawable)

            nativeAdView.callToActionView=context.findViewById(R.id.tv_native_action)
            (nativeAdView.callToActionView as AppCompatTextView).text=it.callToAction
            (nativeAdView.callToActionView as AppCompatTextView).isSelected=type==AdType.TYPE_HOME

            nativeAdView.setNativeAd(it)
            context.findViewById<AppCompatImageView>(R.id.iv_native_default).show(false)
            context.findViewById<AppCompatImageView>(R.id.iv_tag_left).show(type==AdType.TYPE_HOME)
            context.findViewById<AppCompatImageView>(R.id.iv_tag_right).show(type==AdType.TYPE_RESULT)



            LoadAdManager.clearAdCache(type)
            LoadAdManager.loadAd(type)
            context.refreshNativeAd=false
        }
    }

    private fun destroyNativeAd(){
        nativeAd?.let {
            it.destroy()
        }
    }

    fun onDestroy(){
        loopJob?.cancel()
        loopJob=null
    }
}