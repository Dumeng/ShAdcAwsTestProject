package com.example.vincent.shadcmobiletest.amazonaws.mobile.activity

import android.os.Bundle
import android.os.Message
import android.widget.Button
import android.widget.TextView
import com.amazonaws.mobile.client.AWSMobileClient
import com.amazonaws.mobileconnectors.apigateway.ApiClientFactory
import com.example.vincent.shadcmobiletest.AwsApplication
import com.example.vincent.shadcmobiletest.R
import com.example.vincent.shadcmobiletest.amazonaws.mobile.apiClient.HttpClientFactory
import com.example.vincent.shadcmobiletest.amazonaws.mobile.awsClient.ProductMobileHubClient
import com.example.vincent.shadcmobiletest.amazonaws.mobile.base.BaseActivity
import com.example.vincent.shadcmobiletest.amazonaws.mobile.base.BaseMobileHubClien
import com.example.vincent.shadcmobiletest.amazonaws.mobile.model.ProductDO


/**
 * Created by vincent on 02/04/2018.
 */
class CloudTestActivity : BaseActivity() {

    val EMPTY = 0
    val ONE = 1
    var apiClient: BaseMobileHubClien? = null

    var mTestGetFunctionBtn: Button? = null
    var mTestGetOneFunctionBtn: Button? = null
    var mTestCreateFunctionBtn: Button? = null
    var mTestUpdateFunctionBtn: Button? = null
    var mTestDeleteFunctionBtn: Button? = null
    var mLogOutBtn: Button? = null
    var mLogTv: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        TAG = CloudTestActivity::class.java.simpleName
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cloud)
        init()
        initListener()
    }

    fun init() {
        apiClient = ApiClientFactory()
                .credentialsProvider(AWSMobileClient.getInstance().credentialsProvider)
                .build(ProductMobileHubClient::class.java)
        mTestGetFunctionBtn = findViewById(R.id.test_api_btn)
        mTestGetOneFunctionBtn = findViewById<Button>(R.id.test_api_one_btn)
        mTestCreateFunctionBtn = findViewById<Button>(R.id.test_api_create_btn)
        mTestUpdateFunctionBtn = findViewById<Button>(R.id.test_api_update_btn)
        mTestDeleteFunctionBtn = findViewById<Button>(R.id.test_api_delete_btn)
        mLogOutBtn = findViewById<Button>(R.id.button2)
        mLogTv = findViewById(R.id.log_tv)
    }

    fun initListener() {
        mLogOutBtn!!.setOnClickListener {
            AwsApplication.identityManager!!.signOut()
//            android.os.Process.killProcess(android.os.Process.myPid())
//            System.exit(1)
            finish()
        }
//        mTestGetFunctionBtn!!.setOnClickListener({
//            HttpClientFactory.INSTANCE_1.createCallCloudLogic(HttpClientFactory.Method.GET, "", "/product",
//                    apiClient!!.javaClass.simpleName, apiClient!!)
        mTestGetFunctionBtn!!.setOnClickListener {
            HttpClientFactory.INSTANCE_1.createCallCloudLogic(HttpClientFactory.Method.GET, "", "/product",
                    "https://yvnccrnav6.execute-api.us-east-1.amazonaws.com/Development", mHandler)
        }
        mTestGetOneFunctionBtn!!.setOnClickListener {
            val parameters = HashMap<String, String>()
            parameters.put("productId", "027f478fb8dd482dabb9f104bdb42b8b")
            HttpClientFactory.INSTANCE_1.createCallCloudLogic(HttpClientFactory.Method.GET, "", "/product",
                    "https://yvnccrnav6.execute-api.us-east-1.amazonaws.com/Development", mHandler, parameters)
        }
        mTestCreateFunctionBtn!!.setOnClickListener {
            //            createCallCloudLogic()
            var productDO = ProductDO()
            productDO.date = 2.0
            productDO.description = "test product"
            productDO.price = 33.5
            productDO.name = "Vincent's product"
            productDO.userId = null

            val body = productDO.getRequest()
//            HttpClientFactory.INSTANCE_1.createCallCloudLogic(HttpClientFactory.Method.PUT, body, "/product",
//                    ProductMobileHubClient::class.java.simpleName, apiClient!!)
            HttpClientFactory.INSTANCE_1.createCallCloudLogic(HttpClientFactory.Method.PUT, body, "/product",
                    "https://yvnccrnav6.execute-api.us-east-1.amazonaws.com/Development", mHandler)
        }
        mTestUpdateFunctionBtn!!.setOnClickListener {
            var productDO = ProductDO()
            productDO.description = "test product update"
            productDO.price = 406.5
            productDO.userId = null
            val parameters = HashMap<String, String>()
            parameters.put("productId", "027f478fb8dd482dabb9f104bdb42b8b")
            val body = productDO.getRequest()
            HttpClientFactory.INSTANCE_1.createCallCloudLogic(HttpClientFactory.Method.POST, body, "/product",
                    ProductMobileHubClient::class.java.simpleName, apiClient!!, mHandler, parameters)
        }
        mTestDeleteFunctionBtn!!.setOnClickListener {
            val parameters = HashMap<String, String>()
            parameters.put("productId", "027f478fb8dd482dabb9f104bdb42b8b")
            HttpClientFactory.INSTANCE_1.createCallCloudLogic(HttpClientFactory.Method.DELETE, "", "/product",
                    ProductMobileHubClient::class.java.simpleName, apiClient!!, mHandler, parameters)
        }
    }

    override fun dealWithHandlerMessage(msg: Message) {
        when (msg.what) {
            EMPTY -> mLogTv!!.setText(msg.obj.toString())

        }
    }


}