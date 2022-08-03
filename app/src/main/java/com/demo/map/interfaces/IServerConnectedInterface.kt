package com.demo.map.interfaces

import com.github.shadowsocks.bg.BaseService

interface IServerConnectedInterface {
    fun connectState(state:BaseService.State)
}