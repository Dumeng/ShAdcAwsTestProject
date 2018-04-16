package com.example.vincent.shadcmobiletest.amazonaws.mobile.service

import android.content.Intent
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.util.Log
import com.amazonaws.mobileconnectors.pinpoint.targeting.notification.NotificationClient
import com.amazonaws.mobileconnectors.pinpoint.targeting.notification.NotificationDetails
import com.example.vincent.shadcmobiletest.AwsApplication.Companion.pinpointManager
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class NotificationPushService : FirebaseMessagingService() {
    val TAG = NotificationPushService::class.java.simpleName
    /**
     * Helper method to extract SNS message from bundle.
     *
     * @param data bundle
     * @return message string from SNS push notification
     */
    fun getMessage(data: Bundle): String? {
        // If a push notification is sent as plain text, then the message appears in "default".
        // Otherwise it's in the "message" for JSON format.
        return if (data.containsKey("default"))
            data.getString("default")
        else
            data.getString(
                    "message", "")
    }

    private fun broadcast(from: String?, data: Bundle) {
        val intent = Intent(ACTION_PUSH_NOTIFICATION)
        intent.putExtra(INTENT_SNS_NOTIFICATION_FROM, from)
        intent.putExtra(INTENT_SNS_NOTIFICATION_DATA, data)
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
    }

    /**
     * Called when message is received.
     *
     * @param from SenderID of the sender.
     * @param data Data bundle containing message data as key/value pairs. For Set of keys use
     * data.keySet().
     */
    override fun onMessageReceived(message: RemoteMessage) {
        Log.i(TAG,"onMessageReceived----")
        val notificationClient = pinpointManager!!.notificationClient
        val from = message.from
        val data = message.data

        val notificationDetail = NotificationDetails.builder()
                .from(from)
                .mapData(data)
                .intentAction(NotificationClient.FCM_INTENT_ACTION)
                .build()


//        val pushResult = notificationClient.handleGCMCampaignPush(from, data, this.javaClass)
        val pushResult = notificationClient.handleCampaignPush(notificationDetail)
        Log.i(TAG,"pushResult: "+pushResult.toString())
        Log.i(TAG,"data: "+data.toString())

        if (NotificationClient.CampaignPushResult.NOT_HANDLED != pushResult) {
            // The push message was due to a Dartboard campaign.
            // If the app was in the background, a local notification was added in the notification center.
            // If the app was in the foreground, an event was recorded indicating the app was in the foreground,
            // for the demo, we will broadcast the notification to let the main activity display it in a dialog.
            if (NotificationClient.CampaignPushResult.APP_IN_FOREGROUND == pushResult) {
                // Create a message that will display the raw data of the campaign push in a dialog.
                var bundle = Bundle()
                bundle!!.putString("message", String.format("Received Campaign Push:\n%s", data.toString()))
                broadcast(from, bundle)
            }
            return
        }

    }

    companion object {
        val TAG = NotificationPushService::class.java!!.simpleName

        // Intent action used in local broadcast
        val ACTION_PUSH_NOTIFICATION = "push-notification"
        val ACTION_SNS_NOTIFICATION = "sns-notification"
        // Intent keys
        val INTENT_SNS_NOTIFICATION_FROM = "from"
        val INTENT_SNS_NOTIFICATION_DATA = "data"

        /**
         * Helper method to extract push message from bundle.
         *
         * @param data bundle
         * @return message string from push notification
         */
        fun getMessage(data: Bundle): String? {
            // If a push notification is sent as plain
            // text, then the message appears in "default".
            // Otherwise it's in the "message" for JSON format.
            return if (data.containsKey("default"))
                data.getString("default")
            else
                data.getString(
                        "message", "")
        }
    }
}
