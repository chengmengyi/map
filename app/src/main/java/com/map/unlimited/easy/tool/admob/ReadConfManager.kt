package com.map.unlimited.easy.tool.admob

import com.map.unlimited.easy.tool.ext.Config
import com.map.unlimited.easy.tool.ext.initServerList
import com.map.unlimited.easy.tool.server.ServerBean
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.tencent.mmkv.MMKV
import org.json.JSONObject

object ReadConfManager {
    var configCity= arrayListOf<String>()
    var configServer= arrayListOf<ServerBean>()


    fun readConfig(){
//        if (BuildConfig.DEBUG){
//            readCity(Config.LOCAL_CITY)
//            readServer(Config.SERVER_LIST)
//            saveAdConf(Config.LOCAL_AD)
//        }else{
//            val remoteConfig = Firebase.remoteConfig
//            remoteConfig.fetchAndActivate().addOnCompleteListener {
//                if (it.isSuccessful){
//                    readCity(remoteConfig.getString("map_city"))
//                    readServer(remoteConfig.getString("map_server"))
//                    saveAdConf(remoteConfig.getString("map_ad"))
//                }
//            }
//        }
        val remoteConfig = Firebase.remoteConfig
        remoteConfig.fetchAndActivate().addOnCompleteListener {
            if (it.isSuccessful){
                readCity(remoteConfig.getString("map_city"))
                readServer(remoteConfig.getString("map_server"))
                saveAdConf(remoteConfig.getString("map_ad"))
            }
        }
    }

    private fun readCity(string: String){
        try {
            configCity.clear()
            val jsonArray = JSONObject(string).getJSONArray("map_city")
            for (index in 0 until jsonArray.length()){
                configCity.add(jsonArray.optString(index))
            }
        }catch (e:Exception){}
    }

    private fun readServer(string: String){
        try {
            configServer.clear()
            configServer.addAll(initServerList(string))
        }catch (e:Exception){

        }
    }

    private fun saveAdConf(string: String){
        MMKV.defaultMMKV().encode("ad",string)
    }

    fun getAdConf():String{
        val decodeString = MMKV.defaultMMKV().decodeString("ad")
        return if (decodeString.isNullOrEmpty()) Config.LOCAL_AD else decodeString
    }
}