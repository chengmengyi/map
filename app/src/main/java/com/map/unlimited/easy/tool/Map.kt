package com.map.unlimited.easy.tool

import android.app.ActivityManager
import android.app.Application
import com.map.unlimited.easy.tool.ac.HomeActivity
import com.map.unlimited.easy.tool.ext.ActivityLifecycleCallback
import com.map.unlimited.easy.tool.server.ServerManager
import com.github.shadowsocks.Core
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize
import com.tencent.mmkv.MMKV

class Map:Application() {
    override fun onCreate() {
        super.onCreate()
        Core.init(this, HomeActivity::class)
        if (!packageName.equals(getCurrentProcessName(this))){
            return
        }
        Firebase.initialize(this)
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