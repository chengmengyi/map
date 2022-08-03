package com.demo.map.ext

class Config {
    companion object{
        const val URL=""
        const val EMAIL=""

        const val SERVER_LIST="""{
   "map_server":[
      {
         "map_server_pwd":"123456",
         "map_server_method":"chacha20-ietf-poly1305",
         "map_server_port":100,
         "map_server_country":"United States",
         "map_server_city":"New York",
         "map_server_host":"100.223.52.0"
      },
      {
         "map_server_pwd":"123456",
         "map_server_method":"chacha20-ietf-poly1305",
         "map_server_port":100,
         "map_server_country":"Japan",
         "map_server_city":"Sydney",
         "map_server_host":"100.223.52.78"
      }
   ]
}"""

    }
}