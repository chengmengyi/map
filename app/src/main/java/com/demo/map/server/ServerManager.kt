package com.demo.map.server

import com.demo.map.ext.Config
import com.demo.map.ext.initServerList

object ServerManager {
    private val localServerList= arrayListOf<ServerBean>()

    fun getServerList():ArrayList<ServerBean>{
        if (localServerList.isEmpty()){
            localServerList.addAll(initServerList(Config.SERVER_LIST))
        }
        return localServerList
    }

    fun createFastServer()=ServerBean(map_server_country = "Super Fast Server")

}