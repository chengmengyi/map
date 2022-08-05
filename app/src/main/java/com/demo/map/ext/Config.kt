package com.demo.map.ext

class Config {
    companion object{
        const val URL=""
        const val EMAIL=""

        const val LOCAL_CITY="""{
   "map_city":[
      "Atlanta",
      "London"
   ]
}"""

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

        const val LOCAL_AD="""{
    "map_open": [
        {
            "map_source": "admob",
            "map_id": "ca-app-pub-3940256099942544/1033173712x",
            "map_type": "cp",
            "map_sort": 1
        },
        {
            "map_source": "admob",
            "map_id": "ca-app-pub-3940256099942544/3419835294AA",
            "map_type": "kp",
            "map_sort": 2
        },
        {
            "map_source": "admob",
            "map_id": "ca-app-pub-3940256099942544/3419835294",
            "map_type": "kp",
            "map_sort": 3
        }
    ],
    "map_home": [
        {
            "map_source": "admob",
            "map_id": "ca-app-pub-3940256099942544/2247696110",
            "map_type": "ys",
            "map_sort": 2
        },
        {
            "map_source": "admob",
            "map_id": "ca-app-pub-3940256099942544/2247696110",
            "map_type": "ys",
            "map_sort": 1
        },
        {
            "map_source": "admob",
            "map_id": "ca-app-pub-3940256099942544/2247696110AA",
            "map_type": "ys",
            "map_sort": 3
        }
    ],
    "map_result": [
        {
            "map_source": "admob",
            "map_id": "ca-app-pub-3940256099942544/2247696110",
            "map_type": "ys",
            "map_sort": 2
        },
        {
            "map_source": "admob",
            "map_id": "ca-app-pub-3940256099942544/2247696110",
            "map_type": "ys",
            "map_sort": 1
        },
        {
            "map_source": "admob",
            "map_id": "ca-app-pub-3940256099942544/2247696110AA",
            "map_type": "ys",
            "map_sort": 3
        }
    ],
    "map_connect": [
        {
            "map_source": "admob",
            "map_id": "ca-app-pub-3940256099942544/8691691433x",
            "map_type": "cp",
            "map_sort": 2
        },
        {
            "map_source": "admob",
            "map_id": "ca-app-pub-3940256099942544/1033173712AA",
            "map_type": "cp",
            "map_sort": 1
        },
        {
            "map_source": "admob",
            "map_id": "ca-app-pub-3940256099942544/1033173712",
            "map_type": "cp",
            "map_sort": 3
        }
    ],
    "map_back": [
        {
            "map_source": "admob",
            "map_id": "ca-app-pub-3940256099942544/1033173712",
            "map_type": "cp",
            "map_sort": 2
        },
        {
            "map_source": "admob",
            "map_id": "ca-app-pub-3940256099942544/8691691433aa",
            "map_type": "cp",
            "map_sort": 1
        },
        {
            "map_source": "admob",
            "map_id": "ca-app-pub-3940256099942544/1033173712",
            "map_type": "cp",
            "map_sort": 3
        }
    ]
}"""

    }
}