package com.demo.map.server

import com.demo.map.base.BaseActivity
import com.demo.map.interfaces.IServerConnectedInterface
import com.github.shadowsocks.Core
import com.github.shadowsocks.aidl.IShadowsocksService
import com.github.shadowsocks.aidl.ShadowsocksConnection
import com.github.shadowsocks.bg.BaseService
import com.github.shadowsocks.preference.DataStore
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

object ServerConnectCallback: ShadowsocksConnection.Callback{
    private var context:BaseActivity?=null
    private var state=BaseService.State.Idle

    private val ssc = ShadowsocksConnection(true)
    var currentServer=ServerManager.createFastServer()
    var lastServer=ServerManager.createFastServer()
    private var iServerConnectedInterface:IServerConnectedInterface?=null

    fun setIServerConnectedInterface(iServerConnectedInterface:IServerConnectedInterface){
        this.iServerConnectedInterface=iServerConnectedInterface
    }

    fun init(context:BaseActivity){
        this.context=context
        ssc.connect(context,this)
    }

    fun connect(){
        updateState(BaseService.State.Connecting)
        GlobalScope.launch {
            if (currentServer.isFastServer()){
                val bean = ServerManager.getServerList().randomOrNull()
                if (null!=bean){
                    DataStore.profileId = bean.getServerId()
                }
            }else{
                DataStore.profileId = currentServer.getServerId()
            }
            Core.startService()
        }
    }

    fun disConnect(){
        updateState(BaseService.State.Stopping)
        GlobalScope.launch {
            Core.stopService()
        }
    }

    fun isConnected()= state==BaseService.State.Connected

    fun isStopped()= state==BaseService.State.Stopped

    fun updateCurrentServer(serverBean: ServerBean){
        this.currentServer=serverBean
    }

    override fun stateChanged(state: BaseService.State, profileName: String?, msg: String?) {
        updateState(state)
    }

    override fun onServiceConnected(service: IShadowsocksService) {
        updateState(BaseService.State.values()[service.state])
        iServerConnectedInterface?.connectState(state)
    }

    private fun updateState(state:BaseService.State){
        this.state=state
        if (isConnected()){
            lastServer= currentServer
        }
    }

    override fun onBinderDied() {
        context?.run {
            ssc.disconnect(this)
        }
    }

    fun onDestroy(){
        onBinderDied()
        context=null
    }
}