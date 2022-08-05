package com.map.unlimited.easy.tool.ext

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Uri
import android.view.View
import android.widget.Toast
import com.map.unlimited.easy.tool.R
import com.map.unlimited.easy.tool.server.ServerBean
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject

fun View.show(show:Boolean){
    visibility=if (show) View.VISIBLE else View.GONE
}

fun Context.showToast(string:String){
    Toast.makeText(this, string, Toast.LENGTH_LONG).show()
}

fun Context.toEmail(){
    try {
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data= Uri.parse("mailto:")
        intent.putExtra(Intent.EXTRA_EMAIL,Config.EMAIL)
        startActivity(intent)
    }catch (e:Exception){
        showToast("Contact us by email：${Config.EMAIL}")
    }
}

fun getCircleFlagByServer(serverBean: ServerBean):Int{
    when(serverBean.map_server_country){
        "United States"->return R.drawable.icon_flag_circle_usa
        "United Kingdom"->return R.drawable.icon_flag_circle_uk
    }
    return R.drawable.icon_flag_default_circle
}

fun getSquareFlagByServer(serverBean: ServerBean):Int{
    when(serverBean.map_server_country){
        "United States"->return R.drawable.icon_flag_square_usa
        "United Kingdom"->return R.drawable.icon_flag_square_uk
    }
    return R.drawable.icon_flag_default
}


fun transTime(time:Long):String{
    val shi=time/3600
    val fen= (time % 3600) / 60
    val miao= (time % 3600) % 60
    val s=if (shi<10) "0${shi}" else shi
    val f=if (fen<10) "0${fen}" else fen
    val m=if (miao<10) "0${miao}" else miao
    return "${s}:${f}:${m}"
}

fun initServerList(json:String):ArrayList<ServerBean>{
    val list= arrayListOf<ServerBean>()
    try {
        val jsonArray = JSONObject(json).getJSONArray("map_server")
        for (index in 0 until jsonArray.length()){
            val jsonObject = jsonArray.getJSONObject(index)
            list.add(
                ServerBean(
                    jsonObject.optString("map_server_pwd"),
                    jsonObject.optString("map_server_method"),
                    jsonObject.optInt("map_server_port"),
                    jsonObject.optString("map_server_country"),
                    jsonObject.optString("map_server_city"),
                    jsonObject.optString("map_server_host"),
                )
            )
        }
    }catch (e:Exception){

    }
    GlobalScope.launch {
        list.forEach {
            it.writeServerToLocal()
        }
    }

    return list
}

fun getNetWorkStatus(context: Context): Int {
    val connectivityManager = context //连接服务 CONNECTIVITY_SERVICE
        .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetworkInfo = connectivityManager.activeNetworkInfo
    if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
        if (activeNetworkInfo.type == ConnectivityManager.TYPE_WIFI) {
            return 2
        } else if (activeNetworkInfo.type == ConnectivityManager.TYPE_MOBILE) {
            return 0
        }
    } else {
        return 1
    }
    return 1
}