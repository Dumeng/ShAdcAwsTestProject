package com.example.vincent.shadcmobiletest.amazonaws.mobile.base

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v7.app.AppCompatActivity
import com.example.vincent.shadcmobiletest.amazonaws.mobile.analytic.AnalyticUtil

open class BaseActivity : AppCompatActivity() {

    var TAG = BaseActivity::class.java.simpleName
    var mHandler: Handler = object : Handler() {

        override fun handleMessage(msg: Message) {

            super.handleMessage(msg)

            dealWithHandlerMessage(msg)

        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AnalyticUtil.visitPageEvent(TAG)
    }

    open fun dealWithHandlerMessage(msg: Message) {
    }

    override fun onDestroy() {

        super.onDestroy()

        mHandler.removeCallbacksAndMessages(null)

    }

    override fun onPause() {
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
    }

    fun push(mClass: Class<*>) {
        var intent = Intent(this, mClass)
        startActivity(intent)
    }
}
