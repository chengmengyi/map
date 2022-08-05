package com.demo.map.server

import com.demo.map.admob.ReadConfManager
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

    fun getFastServer():ServerBean?{
        if (!ReadConfManager.configCity.isNullOrEmpty()){
            val filter = getServerList().filter { ReadConfManager.configCity.contains(it.map_server_city) }
            if (!filter.isNullOrEmpty()){
                return filter.randomOrNull()
            }
        }
        return getServerList().randomOrNull()
    }

}