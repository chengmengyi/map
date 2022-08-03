package com.demo.map.ac

import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.demo.map.R
import com.demo.map.adapter.ChooseServerAdapter
import com.demo.map.base.BaseActivity
import com.demo.map.event.EventBean
import com.demo.map.event.EventCode
import com.demo.map.server.ServerBean
import com.demo.map.server.ServerConnectCallback
import kotlinx.android.synthetic.main.layout_choose_server.*

class ChooseServerActivity:BaseActivity() {
    override fun layoutId(): Int = R.layout.layout_choose_server

    override fun onView() {
        immersionBar?.statusBarView(top_view)?.init()

        recycler_view.apply {
            layoutManager=LinearLayoutManager(this@ChooseServerActivity)
            adapter=ChooseServerAdapter(this@ChooseServerActivity){
                onClickItem(it)
            }
        }

        iv_back.setOnClickListener { finish() }
    }

    private fun onClickItem(serverBean: ServerBean){
        var result=""
        val connected = ServerConnectCallback.isConnected()
        if (ServerConnectCallback.currentServer.map_server_host==serverBean.map_server_host){
            if (!connected){
                result="connect"
            }
        }else{
            result=if (connected) "disconnect" else "connect"
        }

        if (result=="disconnect"){
            AlertDialog.Builder(this).apply {
                setMessage("You are currently connected and need to disconnect before manually connecting to the server.")
                setPositiveButton("sure", { dialog, which ->
                    sendMsg(result,serverBean)
                })
                setNegativeButton("cancel",null)
                show()
            }
        }else{
            sendMsg(result,serverBean)
        }
    }

    private fun sendMsg(result:String,serverBean: ServerBean){
        ServerConnectCallback.updateCurrentServer(serverBean)
        EventBean(EventCode.CHOOSE_SERVER_BACK,str = result,serverBean = serverBean).send()
        finish()
    }
}