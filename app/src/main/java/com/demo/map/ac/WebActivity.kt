package com.demo.map.ac

import android.webkit.WebView
import android.webkit.WebViewClient
import com.demo.map.R
import com.demo.map.base.BaseActivity
import com.demo.map.ext.Config
import kotlinx.android.synthetic.main.layout_web.*

class WebActivity:BaseActivity(){
    override fun layoutId(): Int = R.layout.layout_web
    override fun onView() {
        immersionBar?.statusBarView(top_view)?.init()
        iv_back.setOnClickListener { finish() }

        web_view.loadUrl(Config.URL)
        web_view.webViewClient=object : WebViewClient(){
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                web_view.loadUrl(url?:"")
                return true
            }
        }
    }
}