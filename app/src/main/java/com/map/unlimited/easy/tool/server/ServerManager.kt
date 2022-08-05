package com.map.unlimited.easy.tool.server

import com.map.unlimited.easy.tool.admob.ReadConfManager
import com.map.unlimited.easy.tool.ext.Config
import com.map.unlimited.easy.tool.ext.initServerList

object ServerManager {
    private val localServerList= arrayListOf<ServerBean>()

    fun initLocalServerList(){
        if (localServerList.isEmpty()){
            localServerList.addAll(initServerList(Config.SERVER_LIST))
        }
    }

    fun getServerList():ArrayList<ServerBean>{
        return ReadConfManager.configServer.ifEmpty { localServerList }
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