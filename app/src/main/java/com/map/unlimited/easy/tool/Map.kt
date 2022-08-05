package com.map.unlimited.easy.tool

import android.app.ActivityManager
import android.app.Application
import com.map.unlimited.easy.tool.ac.HomeActivity
import com.map.unlimited.easy.tool.admob.ReadConfManager
import com.map.unlimited.easy.tool.ext.ActivityLifecycleCallback
import com.map.unlimited.easy.tool.server.ServerManager
import com.github.shadowsocks.Core
import com.google.android.gms.ads.MobileAds
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize
import com.tencent.mmkv.MMKV

lateinit var aMap:Application
class Map:Application() {
    override fun onCreate() {
        super.onCreate()
        aMap=this
        Core.init(this, HomeActivity::class)
        if (!packageName.equals(getCurrentProcessName(this))){
            return
        }
        ActivityLifecycleCallback.register(this)
        MMKV.initialize(this)
        Firebase.initialize(this)
        MobileAds.initialize(this)
        ServerManager.initLocalServerList()
        ReadConfManager.readConfig()
    }

    private fun getCurrentProcessName(applicationContext: Application): String {
        val pid = android.os.Process.myPid()
        var processName = ""
        val manager = applicationContext.getSystemService(Application.ACTIVITY_SERVICE) as ActivityManager
        for (process in manager.runningAppProcesses) {
            if (process.pid === pid) {
                processName = process.processName
            }
        }
        return processName
    }
}