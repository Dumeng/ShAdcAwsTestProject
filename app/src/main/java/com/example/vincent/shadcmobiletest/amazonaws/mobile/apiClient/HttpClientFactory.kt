package com.example.vincent.shadcmobiletest.amazonaws.mobile.apiClient

import android.os.Handler
import android.os.Message
import android.util.Log
import com.amazonaws.http.HttpMethodName
import com.amazonaws.mobile.client.AWSMobileClient
import com.amazonaws.mobileconnectors.apigateway.ApiClientFactory
import com.amazonaws.mobileconnectors.apigateway.ApiRequest
import com.amazonaws.util.IOUtils
import com.amazonaws.util.StringUtils
import com.example.vincent.shadcmobiletest.amazonaws.mobile.base.BaseMobileHubClien

/**
 * Created by vincent on 03/04/2018.
 */

class HttpClientFactory private constructor() {
    val TAG = HttpClientFactory::class.java.simpleName

    companion object {
        //        private var mApiTestAWSMobileClient = ApiClientFactory()
//                .credentialsProvider(AWSMobileClient.getInstance().credentialsProvider)
//                .build(APItestMobileHubClient::class.java)
        //Kotlin原生写法
        val INSTANCE_1 by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            HttpClientFactory()
        }

        //翻译JAVA代码写法
        @Volatile
        private var INSTANCE_2: HttpClientFactory? = null

        fun getInstance(): HttpClientFactory {
            if (INSTANCE_2 == null) {
                synchronized(HttpClientFactory::class) {
                    if (INSTANCE_2 == null) {
                        INSTANCE_2 = HttpClientFactory()
                    }
                }
            }
            return INSTANCE_2!!
        }
    }

    fun createCallCloudLogic(method: Method, body: String? = null, path: String, serviceName: String,
                             baseMobileHubClien: BaseMobileHubClien, handler: Handler, parameters: HashMap<String, String>? = null) {
        // Create components of api request
        Log.i(TAG, "body: " + body)
        val content = body!!.toByteArray(StringUtils.UTF8)
//        body?.let {
//            content = body.toByteArray(StringUtils.UTF8)
//        }
        Log.i(TAG, "content: " + content.toString())

        val headers = HashMap<String, String>()

        // Use components to create the api request
        var localRequest = ApiRequest(serviceName)
                .withPath(path)
                .withHttpMethod(HttpMethodName.valueOf(method.name))
                .withHeaders(headers)
                .addHeader("Content-Type", "application/json")
        parameters?.let {
            localRequest.withParameters(parameters)
        }

        // Only set body if it has content.
        if (body.length > 0) {
            localRequest = localRequest
                    .addHeader("Content-Length", content.size.toString())
                    .withBody(content)
        }

        val request = localRequest

        // Make network call on background thread
        Thread(Runnable {
            try {
                Log.d(TAG,
                        "Invoking API w/ Request : " +
                                request.httpMethod + ":" +
                                request.path)
                val response = baseMobileHubClien!!.execute(request)

                val responseContentStream = response.content
                var responseData: String? = null
                val stringBuilder = StringBuilder()
                if (responseContentStream != null) {
                    responseData = IOUtils.toString(responseContentStream)
                    Log.d(TAG, "Response : $responseData")
                }

                Log.d(TAG, "Response: " + response.statusCode + " " + response.statusText)
                stringBuilder.append("Response : ").append(responseData).append("response.statusCode: ").append(response.statusCode)
                val message = Message.obtain()
                message.what = 0
                message.obj = stringBuilder.toString()
                handler.sendMessage(message)
            } catch (exception: Exception) {
                Log.e(TAG, exception.message, exception)
                exception.printStackTrace()
            }
        }).start()
    }

    fun createCallCloudLogic(method: Method, body: String? = null, path: String, endPoint: String,
                             handler: Handler, parameters: HashMap<String, String>? = null) {
        // Create components of api request

        var serviceName = BaseMobileHubClien::class.java.simpleName
//        var clientConfiguration: ClientConfiguration
//        clientConfiguration.connectionTimeout = 10 * 1000
        var baseMobileHubClien = ApiClientFactory()
//                .clientConfiguration(clientConfiguration)
                .endpoint(endPoint)
                .credentialsProvider(AWSMobileClient.getInstance().credentialsProvider)
                .build(BaseMobileHubClien::class.java)
        Log.i(TAG, "body: " + body)
        val content = body!!.toByteArray(StringUtils.UTF8)
        Log.i(TAG, "content: " + content.toString())
        val headers = HashMap<String, String>()
        // Use components to create the api request
        var localRequest = ApiRequest(serviceName)
                .withPath(path)
                .withHttpMethod(HttpMethodName.valueOf(method.name))
                .withHeaders(headers)
                .addHeader("Content-Type", "application/json")
        parameters?.let {
            localRequest.withParameters(parameters)
        }

        // Only set body if it has content.
        if (body.length > 0) {
            localRequest = localRequest
                    .addHeader("Content-Length", content.size.toString())
                    .withBody(content)
        }
        val request = localRequest
        // Make network call on background thread
        Thread(Runnable {
            try {
                Log.d(TAG,
                        "Invoking API w/ Request : " +
                                request.httpMethod + ":" +
                                request.path)
                val response = baseMobileHubClien!!.execute(request)

                val responseContentStream = response.content
                val stringBuilder = StringBuilder()
                var responseData: String? = null
                if (responseContentStream != null) {
                    responseData = IOUtils.toString(responseContentStream)
                    Log.d(TAG, "Response : $responseData")
                }

                Log.d(TAG, "Response: " + response.statusCode + " " + response.statusText)
                stringBuilder.append("Response : ").append(responseData).append("response.statusCode: ").append(response.statusCode)
                val message = Message.obtain()
                message.what = 0
                message.obj = stringBuilder.toString()
                handler.sendMessage(message)
            } catch (exception: Exception) {
                Log.e(TAG, exception.message, exception)
                exception.printStackTrace()
            }
        }).start()
    }

    enum class Method private constructor(val method: String) {
        GET("GET"),
        PUT("PUT"),
        DELETE("DELETE"),
        POST("POST")
    }
}
