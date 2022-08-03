package com.demo.map.ac

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Intent
import android.net.VpnService
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import com.demo.map.R
import com.demo.map.base.BaseActivity
import com.demo.map.event.EventBean
import com.demo.map.event.EventCode
import com.demo.map.ext.*
import com.demo.map.interfaces.IServerConnectedInterface
import com.demo.map.server.ServerConnectCallback
import com.github.shadowsocks.bg.BaseService
import com.github.shadowsocks.utils.StartService
import com.tencent.mmkv.MMKV
import kotlinx.android.synthetic.main.layout_home.*
import kotlinx.android.synthetic.main.layout_server.*
import kotlinx.android.synthetic.main.layout_set.*
import kotlinx.coroutines.*

class HomeActivity:BaseActivity(), IServerConnectedInterface {
    private var canClick=true
    private var hasPermission = false
    private var objectAnimator: ObjectAnimator?=null

    private var connectTime=0L
    private var connectTimeJob:Job?=null

    private val launcher = registerForActivityResult(StartService()) {
        if (!it && hasPermission) {
            hasPermission = false
            connectServer()
        } else {
            canClick=true
            showToast("Connected fail")
        }
    }

    override fun layoutId(): Int = R.layout.layout_home

    override fun onView() {
        showHome(true)
        connectTime= MMKV.defaultMMKV().decodeLong("time")
        ServerConnectCallback.init(this)
        ServerConnectCallback.setIServerConnectedInterface(this)
        setOnClickListener()
    }

    private fun setOnClickListener(){
        ll_home.setOnClickListener {
            if (canClick){
                showHome(true)
            }
        }
        ll_set.setOnClickListener {
            if (canClick){
                showHome(false)
            }
        }

        ll_server_info_layout.setOnClickListener {
            if (canClick){
                startActivity(Intent(this,ChooseServerActivity::class.java))
            }
        }

        iv_connect_btn.setOnClickListener {
            if (canClick){
                callConnect()
            }
        }

        ll_contact.setOnClickListener {
            if (canClick){
                toEmail()
            }
        }

        ll_privacy.setOnClickListener {
            if (canClick){
                startActivity(Intent(this,WebActivity::class.java))
            }
        }
    }

    private fun callConnect(isConnected:Boolean=ServerConnectCallback.isConnected()){
        canClick=false
        if (isConnected){
            ServerConnectCallback.disConnect()
            updateUI(BaseService.State.Stopping)
            checkConnectStatus(false)
        }else{
            if (getNetWorkStatus(this) ==1){
                canClick=true
                AlertDialog.Builder(this).apply {
                    setMessage("You are not currently connected to the network")
                    setPositiveButton("sure", null)
                    show()
                }
                return
            }
            if (VpnService.prepare(this) != null) {
                hasPermission = true
                launcher.launch(null)
            } else {
                connectServer()
            }
        }
    }

    private fun connectServer(){
        ServerConnectCallback.connect()
        updateUI(BaseService.State.Connecting)
        connectTime=0L
        saveConnectTimeToLocal()
        checkConnectStatus(true)
    }

    private fun checkConnectStatus(isConnect: Boolean){
        lifecycleScope.launch {
            var time = 0
            while (true){
                if (!isActive) {
                    break
                }
                time++
                delay(1000L)
                if (time in 2..10) {
                    val success = if (isConnect) ServerConnectCallback.isConnected() else ServerConnectCallback.isStopped()
                    if (success) {
                        checkConnectResult(isConnect)
                        cancel()
                    }
                } else if (time>10){
                    checkConnectResult(isConnect)
                    cancel()
                }
            }
        }
    }

    private fun checkConnectResult(isConnect: Boolean){
        val success = if (isConnect) ServerConnectCallback.isConnected() else ServerConnectCallback.isStopped()
        if (success){
            if (isConnect){
                updateUI(BaseService.State.Connected)
            }else{
                updateUI(BaseService.State.Stopped)
                updateCurrentServerInfo()
            }
            if (ActivityLifecycleCallback.isFront){
                val intent = Intent(this, ConnectResultActivity::class.java)
                intent.putExtra("state",isConnect)
                intent.putExtra("time",connectTime)
                startActivity(intent)
            }
            canClick=true
        }else{
            showToast(if (isConnect) "Connect Fail" else "Disconnect Fail")
            updateUI(BaseService.State.Idle)
            canClick=true
        }
    }

    private fun updateUI(state:BaseService.State){
        stopConnectBtnAnimator()
        when(state){
            BaseService.State.Idle,BaseService.State.Stopped->{
                connecting_lottie_view.show(false)
                cl_connected_layout.show(false)
                iv_un_connect_layout.show(true)
                setConnectStatusIcon(R.drawable.icon_idle,R.drawable.icon_connect_btn)
            }
            BaseService.State.Connecting,BaseService.State.Stopping->{
                startConnectBtnAnimator()
                connecting_lottie_view.show(true)
                cl_connected_layout.show(false)
                iv_un_connect_layout.show(false)
            }
            BaseService.State.Connected->{
                updateConnectedUI()
            }
        }
    }

    private fun updateConnectedUI(){
        connecting_lottie_view.show(false)
        cl_connected_layout.show(true)
        iv_un_connect_layout.show(false)
        val currentServer = ServerConnectCallback.currentServer
        iv_center_flag.setImageResource(getCircleFlagByServer(currentServer))
        startCountConnectTime()
        setConnectStatusIcon(R.drawable.icon_connected,R.drawable.icon_connected_btn)
    }

    private fun startCountConnectTime(){
        stopCountConnectTime()
        connectTimeJob=GlobalScope.launch(Dispatchers.Main) {
            while (true){
                delay(1000L)
                connectTime++
                tv_center_time.text= transTime(connectTime)
            }
        }
    }

    private fun stopCountConnectTime(){
        connectTimeJob?.cancel()
        connectTimeJob=null
    }

    private fun updateCurrentServerInfo(){
        val currentServer = ServerConnectCallback.currentServer
        tv_server_info_name.text=currentServer.map_server_country
        iv_server_info_flag.setImageResource(getCircleFlagByServer(currentServer))
    }

    private fun startConnectBtnAnimator(){
        setConnectStatusIcon(R.drawable.icon_connecting,R.drawable.icon_connect_btn)
        objectAnimator=ObjectAnimator.ofFloat(iv_connect_status, "rotation", 0f, 360f).apply {
            duration=1000L
            repeatCount= ValueAnimator.INFINITE
            repeatMode=ObjectAnimator.RESTART
            start()
        }
    }

    private fun setConnectStatusIcon(statusResId:Int,btnResId:Int){
        iv_connect_status.setImageResource(statusResId)
        iv_connect_btn.setImageResource(btnResId)
    }

    private fun stopConnectBtnAnimator(){
        iv_connect_status.rotation=0F
        objectAnimator?.cancel()
        objectAnimator=null
    }

    private fun showHome(showHome:Boolean){
        server_layout.show(showHome)
        set_layout.show(!showHome)
        iv_bottom_home.isSelected=showHome
        iv_bottom_set.isSelected=!showHome
    }

    override fun onEventOtherMsg(bean: EventBean) {
        super.onEventOtherMsg(bean)
        if (bean.code==EventCode.CHOOSE_SERVER_BACK){
            when(bean.str){
                "connect"->{
                    updateCurrentServerInfo()
                    callConnect(false)
                }
                "disconnect"->{
                    callConnect(true)
                }
            }
        }
    }

    private fun saveConnectTimeToLocal(){
        MMKV.defaultMMKV().encode("time",connectTime)
    }

    override fun connectState(state: BaseService.State) {
        when(state){
            BaseService.State.Connected->updateConnectedUI()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        saveConnectTimeToLocal()
        ServerConnectCallback.onDestroy()
        stopConnectBtnAnimator()
        stopCountConnectTime()
    }
}