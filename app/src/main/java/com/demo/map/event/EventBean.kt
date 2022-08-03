package com.demo.map.event

import com.demo.map.server.ServerBean
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