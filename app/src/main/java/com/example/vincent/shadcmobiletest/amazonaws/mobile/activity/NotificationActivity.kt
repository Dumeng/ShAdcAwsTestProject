package com.example.vincent.shadcmobiletest.amazonaws.mobile.activity

import android.app.AlertDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.util.Log
import com.baidu.android.pushservice.PushConstants
import com.baidu.android.pushservice.PushManager
import com.baidu.android.pushservice.PushSettings
import com.example.vincent.shadcmobiletest.R
import com.example.vincent.shadcmobiletest.amazonaws.mobile.base.BaseActivity
import com.example.vincent.shadcmobiletest.amazonaws.mobile.service.NotificationPushService
import com.example.vincent.shadcmobiletest.amazonaws.mobile.util.BaiduUtils


class NotificationActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TAG = NotificationActivity::class.java.simpleName
        setContentView(R.layout.activity_push)
//        initPush()
        initReceiver()
//        initBaiduReceiver()
    }

    fun initReceiver() {
        LocalBroadcastManager.getInstance(this).registerReceiver(notificationReceiver,
                IntentFilter(NotificationPushService.ACTION_PUSH_NOTIFICATION))

    }

    fun initBaiduReceiver() {
        // Push: 以apikey的方式登录，一般放在主Activity的onCreate中。
        // 这里把apikey存放于manifest文件中，只是一种存放方式，
        // 您可以用自定义常量等其它方式实现，来替换参数中的Utils.getMetaValue(PushDemoActivity.this,
        // "api_key")
        // ！！请将AndroidManifest.xml api_key 字段值修改为自己的 api_key 方可使用 ！！
        // ！！ATTENTION：You need to modify the value of api_key to your own in AndroidManifest.xml to use this Demo !!
        // 启动百度push
        val apiKey = BaiduUtils.getMetaValue(this@NotificationActivity, "api_key")
        Log.i(TAG, "apikey: " + apiKey)
        PushSettings.enableDebugMode(this,true)
        PushManager.startWork(application, PushConstants.LOGIN_TYPE_API_KEY, apiKey)
        // Push: 如果想基于地理位置推送，可以打开支持地理位置的推送的开关
//        PushManager.enableLbs(application)
    }

    private val notificationReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            Log.d(TAG, "Received notification from local broadcast. Display it in a dialog.")

            val data = intent.getBundleExtra(NotificationPushService.INTENT_SNS_NOTIFICATION_DATA)
            val message = NotificationPushService.getMessage(data)

            AlertDialog.Builder(this@NotificationActivity)
                    .setTitle("Push notification")
                    .setMessage(message)
                    .setPositiveButton(android.R.string.ok, null)
                    .show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(notificationReceiver);
    }

}
