package com.map.unlimited.easy.tool.interfaces

import com.github.shadowsocks.bg.BaseService

interface IServerConnectedInterface {
    fun connectState(state:BaseService.State)
}