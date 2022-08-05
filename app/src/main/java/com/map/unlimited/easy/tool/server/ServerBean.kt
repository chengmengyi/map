package com.map.unlimited.easy.tool.server

import com.github.shadowsocks.database.Profile
import com.github.shadowsocks.database.ProfileManager

data class ServerBean(
    val map_server_pwd:String="",
    val map_server_method:String="",
    val map_server_port:Int=0,
    val map_server_country:String="",
    val map_server_city:String="",
    val map_server_host:String="",
) {

    fun isFastServer() = map_server_country=="Super Fast Server"

    fun getServerId():Long{
        ProfileManager.getActiveProfiles()?.forEach {
            if (it.host==map_server_host&&it.remotePort==map_server_port){
                return it.id
            }
        }
        return 0L
    }

    fun writeServerToLocal(){
        val profile = Profile(
            id = 0L,
            name = "$map_server_country - $map_server_city",
            host = map_server_host,
            remotePort = map_server_port,
            password = map_server_pwd,
            method = map_server_method
        )

        var id:Long?=null
        ProfileManager.getActiveProfiles()?.forEach {
            if (it.remotePort==profile.remotePort&&it.host==profile.host){
                id=it.id
                return@forEach
            }
        }
        if (null==id){
            ProfileManager.createProfile(profile)
        }else{
            profile.id=id!!
            ProfileManager.updateProfile(profile)
        }
    }
}