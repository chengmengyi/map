package com.map.unlimited.easy.tool.admob

data class ConfAdBean(
    val map_source:String,
    val map_id:String,
    val map_type:String,
    val map_sort:Int,
) {
    override fun toString(): String {
        return "ConfAdBean(map_source='$map_source', map_id='$map_id', map_type='$map_type', map_sort=$map_sort)"
    }
}