package com.demo.map.ext

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.os.Bundle
import com.blankj.utilcode.util.ActivityUtils
import com.demo.map.ac.HomeActivity
import com.demo.map.ac.MainActivity
import com.demo.map.admob.LoadAdManager
import com.demo.map.event.EventBean
import com.demo.map.event.EventCode
import com.google.android.gms.ads.AdActivity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus

object ActivityLifecycleCallback {
    var isFront=true
    private var job: Job?=null
    private var reload=false

    fun register(application: Application){
        application.registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks{
            private var num=0
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}

            override fun onActivityStarted(activity: Activity) {
                num++
                job?.cancel()
                job=null
                if (num==1){
                    isFront=true
                    if (reload){
                        EventBean(EventCode.REFRESH_NATIVE_AD).send()
                        printMap("==reload==")
                        if (ActivityUtils.isActivityExistsInStack(HomeActivity::class.java)){
                            activity.startActivity(Intent(activity, MainActivity::class.java))
                        }
                    }
                    reload=false
                }
            }

            override fun onActivityResumed(activity: Activity) {}

            override fun onActivityPaused(activity: Activity) {}

            override fun onActivityStopped(activity: Activity) {
                num--
                if (num<=0){
                    isFront=true
                    job= GlobalScope.launch {
                        delay(3000L)
                        reload=true
                        LoadAdManager.isShowingFullAd=false
                        ActivityUtils.finishActivity(MainActivity::class.java)
                        ActivityUtils.finishActivity(AdActivity::class.java)
                    }
                }
            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}

            override fun onActivityDestroyed(activity: Activity) {}
        })
    }
}