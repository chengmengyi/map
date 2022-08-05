package com.demo.map.admob

data class AdDataBean (
    val ad:Any?=null,
    val loadTime:Long=0
) {

    fun isExpired() = (System.currentTimeMillis() - loadTime) >= 60L * 60L * 1000L
}