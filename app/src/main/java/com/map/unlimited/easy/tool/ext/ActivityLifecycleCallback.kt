package com.map.unlimited.easy.tool.ext

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.os.Bundle
import com.blankj.utilcode.util.ActivityUtils
import com.map.unlimited.easy.tool.ac.HomeActivity
import com.map.unlimited.easy.tool.ac.MainActivity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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
                        ActivityUtils.finishActivity(MainActivity::class.java)
                    }
                }
            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}

            override fun onActivityDestroyed(activity: Activity) {}
        })
    }
}