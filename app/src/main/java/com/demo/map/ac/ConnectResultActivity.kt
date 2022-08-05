package com.demo.map.ac

import com.demo.map.R
import com.demo.map.admob.AdType
import com.demo.map.admob.ShowNativeAd
import com.demo.map.base.BaseActivity
import com.demo.map.ext.getSquareFlagByServer
import com.demo.map.ext.transTime
import com.demo.map.server.ServerConnectCallback
import kotlinx.android.synthetic.main.layout_connect_result.*
import kotlinx.coroutines.*

class ConnectResultActivity :BaseActivity(){
    private var connectTimeJob: Job?=null
    private val showResultNativeAdManager by lazy { ShowNativeAd(this, AdType.TYPE_RESULT) }

    override fun layoutId(): Int = R.layout.layout_connect_result

    override fun onView() {
        immersionBar?.statusBarView(top_view)?.init()

        val state = intent.getBooleanExtra("state", false)
        root_layout.isSelected=state
        iv_connect_status.isSelected=state
        tv_connect_time.isSelected=state
        tv_connect_status_text.text=if (state) "Connected！" else "Disconnected！"

        val lastServer = ServerConnectCallback.lastServer
        iv_server_flag.setImageResource(getSquareFlagByServer(lastServer))
        tv_server_name.text=lastServer.map_server_country
        startCountConnectTime(state)

        iv_back.setOnClickListener { finish() }
    }

    private fun startCountConnectTime(state:Boolean){
        var time = intent.getLongExtra("time", 0L)
        tv_connect_time.text= transTime(time)
        if (state){
            connectTimeJob= GlobalScope.launch(Dispatchers.Main) {
                while (true){
                    delay(1000L)
                    time++
                    tv_connect_time.text= transTime(time)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (refreshNativeAd){
            showResultNativeAdManager.loopGetNativeAd()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        connectTimeJob?.cancel()
        connectTimeJob=null
        showResultNativeAdManager.onDestroy()
    }
}