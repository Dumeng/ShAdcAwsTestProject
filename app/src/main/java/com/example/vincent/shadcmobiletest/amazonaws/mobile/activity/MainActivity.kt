package com.example.vincent.shadcmobiletest.amazonaws.mobile.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import com.amazonaws.AmazonClientException
import com.amazonaws.mobile.auth.core.IdentityManager
import com.amazonaws.mobileconnectors.pinpoint.PinpointConfiguration
import com.amazonaws.mobileconnectors.pinpoint.PinpointManager
import com.amazonaws.services.pinpoint.model.ChannelType
import com.baidu.android.pushservice.PushConstants
import com.baidu.android.pushservice.PushManager
import com.baidu.android.pushservice.PushSettings
import com.example.vincent.shadcmobiletest.AwsApplication
import com.example.vincent.shadcmobiletest.AwsApplication.Companion.pinpointManager
import com.example.vincent.shadcmobiletest.R
import com.example.vincent.shadcmobiletest.amazonaws.mobile.analytic.AnalyticUtil
import com.example.vincent.shadcmobiletest.amazonaws.mobile.analytic.AnalyticUtil.JUMP_TO_CLOUD_PAGE
import com.example.vincent.shadcmobiletest.amazonaws.mobile.analytic.AnalyticUtil.JUMP_TO_NOSQL_PAGE
import com.example.vincent.shadcmobiletest.amazonaws.mobile.analytic.AnalyticUtil.JUMP_TO_NOTIFICATION_PAGE
import com.example.vincent.shadcmobiletest.amazonaws.mobile.base.BaseActivity
import com.example.vincent.shadcmobiletest.amazonaws.mobile.util.BaiduUtils
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging


/**
 * Created by vincent on 08/04/2018.
 */
class MainActivity : BaseActivity(), View.OnClickListener {
    private var mCloudBtn: Button? = null
    private var mLocalBtn: Button? = null
    private var mNotificationBtn: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        TAG = MainActivity::class.java.simpleName
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
        initListener()
        isNeedInitBaidu()
    }

    fun init() {
        mCloudBtn = findViewById(R.id.cloud_btn)
        mLocalBtn = findViewById(R.id.nosql_btn)
        mNotificationBtn = findViewById(R.id.tocification_btn)
        val userId = AwsApplication.identityManager!!.cachedUserID
        val endpointId = pinpointManager!!.targetingClient.currentEndpoint().endpointId
        Log.i(TAG, "userId---" + userId)
        Log.i(TAG, "endpointId---" + endpointId)
    }

    fun isNeedInitBaidu() {
        val token = FirebaseInstanceId.getInstance().token
        Log.d(TAG, "Refreshed token: $token")
        if (token == null) {
            FirebaseMessaging.getInstance().isAutoInitEnabled = false
            changePinPoint(ChannelType.BAIDU)
            initBaiduReceiver()
        }
    }

    fun changePinPoint(channelType: ChannelType) {
        try {
            val credentialsProvider =
                    IdentityManager.getDefaultIdentityManager().credentialsProvider
            val pinpointConfig = PinpointConfiguration(
                    this,
                    credentialsProvider,
                    AwsApplication.awsConfiguration)
                    .withChannelType(channelType)
            AwsApplication.pinpointManager = PinpointManager(pinpointConfig)
        } catch (ex: AmazonClientException) {
            Log.e(TAG, "Unable to initialize PinpointManager. " + ex.message, ex)
        }
    }

    fun initBaiduReceiver() {
        // 启动百度push
        val apiKey = BaiduUtils.getMetaValue(application, "api_key")
        Log.i(TAG, "apikey: " + apiKey)
        PushSettings.enableDebugMode(this, true)
        PushManager.startWork(application, PushConstants.LOGIN_TYPE_API_KEY, apiKey)
        // Push: 如果想基于地理位置推送，可以打开支持地理位置的推送的开关
//        PushManager.enableLbs(application)
    }

    fun initListener() {
        mCloudBtn!!.setOnClickListener(this)
        mLocalBtn!!.setOnClickListener(this)
        mNotificationBtn!!.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.cloud_btn -> {
                push(CloudTestActivity::class.java)
                AnalyticUtil.submitEvent(AnalyticUtil.EventType.CLICK_EVENT, JUMP_TO_CLOUD_PAGE)
            }
            R.id.nosql_btn -> {
                push(NoSqlTestActivity::class.java)
                AnalyticUtil.submitEvent(AnalyticUtil.EventType.CLICK_EVENT, JUMP_TO_NOSQL_PAGE)
            }
            R.id.tocification_btn -> {
                push(NotificationActivity::class.java)
                AnalyticUtil.submitEvent(AnalyticUtil.EventType.CLICK_EVENT, JUMP_TO_NOTIFICATION_PAGE)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(TAG, "MainActivity destory----")
    }

}