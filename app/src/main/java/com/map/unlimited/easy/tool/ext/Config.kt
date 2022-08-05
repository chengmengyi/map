package com.map.unlimited.easy.tool.ext

class Config {
    companion object{
        const val URL="https://sites.google.com/view/mapapp/%E9%A6%96%E9%A0%81"
        const val EMAIL="cuiqian973@gmail.com"

        const val LOCAL_CITY="""{
   "map_city":[
      "Atlanta",
      "London"
   ]
}"""

        const val SERVER_LIST="""{
    "map_server":[
       {
          "map_server_pwd":"wcGm8qA0NWa0C4l0Fkal",
          "map_server_method":"chacha20-ietf-poly1305",
          "map_server_port":1945,
          "map_server_country":"United States",
          "map_server_city":"Atlanta",
          "map_server_host":"31.13.213.155"
       },
       {
          "map_server_pwd":"wcGm8qA0NWa0C4l0Fkal",
          "map_server_method":"chacha20-ietf-poly1305",
          "map_server_port":1945,
          "map_server_country":"United Kingdom",
          "map_server_city":"London",
          "map_server_host":"5.8.33.49"
       }
    ]
 }"""

        const val LOCAL_AD="""{
    "map_open": [
        {
            "map_source": "admob",
            "map_id": "ca-app-pub-8933525717079761/8678406840",
            "map_type": "kp",
            "map_sort": 2
        }
    ],
    "map_home": [
        {
            "map_source": "admob",
            "map_id": "ca-app-pub-8933525717079761/4683321514",
            "map_type": "ys",
            "map_sort": 2
        }
    ],
    "map_result": [
        {
            "map_source": "admob",
            "map_id": "ca-app-pub-8933525717079761/5804831498",
            "map_type": "ys",
            "map_sort": 2
        }
    ],
    "map_connect": [
        {
            "map_source": "admob",
            "map_id": "ca-app-pub-8933525717079761/3178668157",
            "map_type": "cp",
            "map_sort": 2
        }
    ],
    "map_back": [
        {
            "map_source": "admob",
            "map_id": "ca-app-pub-8933525717079761/4300178139",
            "map_type": "cp",
            "map_sort": 2
        }
    ]
}"""

    }
}