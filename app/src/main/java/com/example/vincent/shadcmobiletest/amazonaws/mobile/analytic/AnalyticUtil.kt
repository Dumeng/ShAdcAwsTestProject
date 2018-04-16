package com.example.vincent.shadcmobiletest.amazonaws.mobile.analytic

import com.amazonaws.mobileconnectors.pinpoint.analytics.monetization.AmazonMonetizationEventBuilder
import com.example.vincent.shadcmobiletest.AwsApplication.Companion.pinpointManager
import java.text.SimpleDateFormat
import java.util.*

object AnalyticUtil {

    private val currentTime: String
        get() {
            val formatter = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
            val date = Date(System.currentTimeMillis())
            return formatter.format(date)
        }

    val JUMP_TO_CLOUD_PAGE ="jump_to_cloud_page"
    val JUMP_TO_NOSQL_PAGE ="jump_to_nosql_page"
    val JUMP_TO_NOTIFICATION_PAGE ="jump_to_notification_page"

    fun pauseSession() {
        pinpointManager!!.sessionClient.pauseSession()
    }

    fun resumeSession() {
        pinpointManager!!.sessionClient.resumeSession()
    }

    fun startSession() {
        pinpointManager!!.sessionClient.startSession()
    }

    fun stopSession() {
        pinpointManager!!.sessionClient.stopSession()
    }

    fun visitPageEvent(pageName: String) {
        val event = pinpointManager!!.analyticsClient.createEvent(EventType.VISIT_PAGE_EVENT.eventType)
                .withAttribute("pageName", pageName)
//                .withAttribute("timeStamp", currentTime)
//                .withMetric("DemoMetric1", Math.random())
        pinpointManager!!.analyticsClient.recordEvent(event)
        pinpointManager!!.analyticsClient.submitEvents()
    }

    fun submitEvent(eventType: EventType, eventMsg: String) {
        val event = pinpointManager!!.analyticsClient.createEvent(eventType.eventType)
                .withAttribute("eventMsg", eventMsg)
//                .withAttribute("timeStamp", currentTime)
//                .withMetric("DemoMetric1", Math.random())
        pinpointManager!!.analyticsClient.recordEvent(event)
        pinpointManager!!.analyticsClient.submitEvents()
    }

    fun monetizationEvent() {
        val event = AmazonMonetizationEventBuilder.create(pinpointManager!!.analyticsClient)
                .withFormattedItemPrice("$10.00")
                .withProductId("DEMO_PRODUCT_ID")
                .withQuantity(1.0)
                .withProductId("DEMO_TRANSACTION_ID").build()

        pinpointManager!!.analyticsClient.recordEvent(event)
        pinpointManager!!.analyticsClient.submitEvents()
    }

    enum class EventType constructor(val eventType: String) {
        /**
         * Main click button
         */
        CLICK_EVENT("clickable_event"),
        VISIT_PAGE_EVENT("visit_page_event")
    }

}
