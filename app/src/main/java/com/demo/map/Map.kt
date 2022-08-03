package com.demo.map

import android.app.ActivityManager
import android.app.Application
import com.demo.map.ac.HomeActivity
import com.demo.map.ext.ActivityLifecycleCallback
import com.demo.map.server.ServerManager
import com.github.shadowsocks.Core
import com.tencent.mmkv.MMKV

class Map:Application() {
    override fun onCreate() {
        super.onCreate()
        Core.init(this, HomeActivity::class)
        if (!packageName.equals(getCurrentProcessName(this))){
            return
        }
        ActivityLifecycleCallback.register(this)
        MMKV.initialize(this)
        ServerManager.getServerList()
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