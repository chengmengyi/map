package com.map.unlimited.easy.tool.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.map.unlimited.easy.tool.R
import com.map.unlimited.easy.tool.ext.getSquareFlagByServer
import com.map.unlimited.easy.tool.ext.show
import com.map.unlimited.easy.tool.server.ServerBean
import com.map.unlimited.easy.tool.server.ServerConnectCallback
import com.map.unlimited.easy.tool.server.ServerManager
import kotlinx.android.synthetic.main.layout_choose_server_item.view.*

class ChooseServerAdapter(
    private val ctx:Context,
    private val onClick:(bean:ServerBean)->Unit
):RecyclerView.Adapter<ChooseServerAdapter.MyView>() {
    private val list= arrayListOf<ServerBean>()
    init {
        list.add(ServerManager.createFastServer())
        list.addAll(ServerManager.getServerList())
    }

    inner class MyView(view:View):RecyclerView.ViewHolder(view){
        init {
            view.setOnClickListener {
                onClick(list[layoutPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyView =
        MyView(LayoutInflater.from(ctx).inflate(R.layout.layout_choose_server_item,parent,false))

    override fun onBindViewHolder(holder: MyView, position: Int) {
        with(holder.itemView){
            val serverBean = list[position]
            tv_server_name.text=serverBean.map_server_country
            iv_server_flag.setImageResource(getSquareFlagByServer(serverBean))
            val isSelect = ServerConnectCallback.currentServer.map_server_host == serverBean.map_server_host
            root_layout.isSelected=isSelect
            tv_server_name.isSelected=isSelect
            iv_select.show(isSelect)
        }
    }

    override fun getItemCount(): Int = list.size
}