package com.map.unlimited.easy.tool.base

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.DisplayMetrics
import androidx.appcompat.app.AppCompatActivity
import com.map.unlimited.easy.tool.event.EventBean
import com.map.unlimited.easy.tool.event.EventCode
import com.gyf.immersionbar.ImmersionBar
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

abstract class BaseActivity:AppCompatActivity() {
    private var resume=false
    var refreshNativeAd=true
    protected var immersionBar:ImmersionBar?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        density()
        setContentView(layoutId())
        EventBus.getDefault().register(this)
        immersionBar=ImmersionBar.with(this).apply {
            statusBarAlpha(0f)
            autoDarkModeEnable(true)
            statusBarDarkFont(true)
            init()
        }
        onView()
    }

    abstract fun layoutId():Int

    abstract fun onView()

    private fun density(){
        val metrics: DisplayMetrics = resources.displayMetrics
        val td = metrics.heightPixels / 760f
        val dpi = (160 * td).toInt()
        metrics.density = td
        metrics.scaledDensity = td
        metrics.densityDpi = dpi
    }

    fun getResume()=resume

    override fun onResume() {
        super.onResume()
        resume=true
    }

    override fun onPause() {
        super.onPause()
        resume=false
    }

    override fun onStop() {
        super.onStop()
        resume=false
    }


    @Subscribe
    fun onEvent(bean: EventBean) {
        if (bean.code==EventCode.REFRESH_NATIVE_AD){
            refreshNativeAd=true
        }else{
            onEventOtherMsg(bean)
        }
    }

    protected open fun onEventOtherMsg(bean: EventBean){}

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }
}