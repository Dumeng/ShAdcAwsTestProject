package com.example.vincent.shadcmobiletest.amazonaws.mobile.service

import android.util.Log
import com.example.vincent.shadcmobiletest.AwsApplication
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.FirebaseInstanceIdService


class MyInstanceIDListenerService : FirebaseInstanceIdService() {
    val TAG = MyInstanceIDListenerService::class.java.simpleName
    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is also called
     * when the InstanceID token is initially generated, so this is where
     * you retrieve the token.
     */
    // [START refresh_token]
    override fun onTokenRefresh() {
        // Get updated InstanceID token.
        val refreshedToken = FirebaseInstanceId.getInstance().token
        Log.d(TAG, "Refreshed token: $refreshedToken")
        // TODO: Implement this method to send any registration to your app's servers.
//        sendRegistrationToServer(refreshedToken)
        updateGCMToken(refreshedToken!!)
    }

    fun updateGCMToken(refreshedToken: String) {
        Thread(Runnable {
            try {
                AwsApplication.pinpointManager!!.notificationClient
                        .registerDeviceToken(refreshedToken)
            } catch (e: Throwable) {
                Log.i(TAG, "register exception: " + e.toString())
                e.printStackTrace()
            }

        }).start()
    }
}
