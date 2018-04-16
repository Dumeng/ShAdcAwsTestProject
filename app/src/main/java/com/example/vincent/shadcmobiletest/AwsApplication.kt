package com.example.vincent.shadcmobiletest

import android.support.multidex.MultiDexApplication
import android.util.Log
import com.amazonaws.AmazonClientException
import com.amazonaws.mobile.auth.core.IdentityManager
import com.amazonaws.mobile.auth.userpools.CognitoUserPoolsSignInProvider
import com.amazonaws.mobile.config.AWSConfiguration
import com.amazonaws.mobileconnectors.pinpoint.PinpointConfiguration
import com.amazonaws.mobileconnectors.pinpoint.PinpointManager
import com.example.vincent.shadcmobiletest.amazonaws.mobile.analytic.AnalyticUtil
import com.example.vincent.shadcmobiletest.amazonaws.mobile.util.AbstractApplicationLifeCycleHelper


/**
 * Created by vincent on 30/03/2018.
 */

class AwsApplication : MultiDexApplication() {
    val TAG = AwsApplication::class.java.simpleName
    private var applicationLifeCycleHelper: AbstractApplicationLifeCycleHelper? = null

    override fun onCreate() {
        super.onCreate()

        initializeApplication()
        initializePinPoint()
        initLifeCycleHelper()
    }

    fun initLifeCycleHelper() {
        // The Helper registers itself to receive application lifecycle events when it is constructed.
        // A reference is kept here in order to pass through the onTrimMemory() call from
        // the Application class to properly track when the application enters the background.
        applicationLifeCycleHelper = object : AbstractApplicationLifeCycleHelper(this) {
            override fun applicationEnteredForeground() {
                Log.d(TAG, "Detected application has entered the applicationEnteredForeground.")
                AnalyticUtil.startSession()
                // handle any events that should occur when your app has come to the foreground...
            }

            override fun applicationEnteredBackground() {
                Log.d(TAG, "Detected application has entered the background.")
                AnalyticUtil.stopSession()
                pinpointManager!!.analyticsClient.submitEvents()

                // handle any events that should occur when your app has gone into the background...
            }
        }
    }

    fun initializeApplication() {
        awsConfiguration = AWSConfiguration(applicationContext)
        // If IdentityManager is not created, create it
        if (IdentityManager.getDefaultIdentityManager() == null) {
            identityManager = IdentityManager(applicationContext, awsConfiguration)
            IdentityManager.setDefaultIdentityManager(identityManager)
        }
        // Add Amazon Cognito User Pools as Identity Provider.
        IdentityManager.getDefaultIdentityManager().addSignInProvider(
                CognitoUserPoolsSignInProvider::class.java)
    }

    fun initializePinPoint() {
        try {
            val credentialsProvider =
                    IdentityManager.getDefaultIdentityManager().credentialsProvider
            val pinpointConfig = PinpointConfiguration(
                    this,
                    credentialsProvider,
                    awsConfiguration)
            pinpointManager = PinpointManager(pinpointConfig)
        } catch (ex: AmazonClientException) {
            Log.e(TAG, "Unable to initialize PinpointManager. " + ex.message, ex)
        }
    }

    override fun onTrimMemory(level: Int) {
        Log.d(TAG, "onTrimMemory $level")
        applicationLifeCycleHelper!!.handleOnTrimMemory(level)
        super.onTrimMemory(level)
    }


    companion object {
        var identityManager: IdentityManager? = null
        var pinpointManager: PinpointManager? = null
        var awsConfiguration: AWSConfiguration? = null
        var application = this
    }
}
