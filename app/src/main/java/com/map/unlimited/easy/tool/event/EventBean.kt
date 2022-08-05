package com.map.unlimited.easy.tool.event

import com.map.unlimited.easy.tool.server.ServerBean
import org.greenrobot.eventbus.EventBus

data class EventBean(
    val code:Int,
    val str:String="",
    val serverBean: ServerBean?=null
) {
    fun send(){
        EventBus.getDefault().post(this)
    }
}