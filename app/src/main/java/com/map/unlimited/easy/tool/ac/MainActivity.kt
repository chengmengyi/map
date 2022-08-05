package com.map.unlimited.easy.tool.ac

import android.animation.ValueAnimator
import android.content.Intent
import android.view.KeyEvent
import android.view.animation.LinearInterpolator
import androidx.core.animation.doOnEnd
import com.blankj.utilcode.util.ActivityUtils
import com.map.unlimited.easy.tool.R
import com.map.unlimited.easy.tool.base.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {
    private var valueAnimator:ValueAnimator?=null

    override fun layoutId(): Int = R.layout.activity_main

    override fun onView() {
        valueAnimator = ValueAnimator.ofInt(0, 100).apply {
            duration=2000L
            interpolator = LinearInterpolator()
            addUpdateListener {
                val pro = it.animatedValue as Int
                progress_bar.progress = pro
            }
            doOnEnd {
                startHome()
            }
            start()
        }
    }

    private fun startHome(){
        if (!ActivityUtils.isActivityExistsInStack(HomeActivity::class.java)){
            startActivity(Intent(this,HomeActivity::class.java))
        }
        finish()
    }

    override fun onResume() {
        super.onResume()
        valueAnimator?.resume()
    }

    override fun onPause() {
        super.onPause()
        valueAnimator?.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        valueAnimator?.removeAllUpdateListeners()
        valueAnimator?.cancel()
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode== KeyEvent.KEYCODE_BACK){
            return true
        }
        return false
    }
}