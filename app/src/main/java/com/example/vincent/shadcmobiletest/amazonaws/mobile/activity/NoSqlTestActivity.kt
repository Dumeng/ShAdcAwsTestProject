package com.example.vincent.shadcmobiletest.amazonaws.mobile.activity

import android.os.Bundle
import android.os.Message
import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.amazonaws.mobile.client.AWSMobileClient
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBQueryExpression
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient
import com.example.vincent.shadcmobiletest.AwsApplication
import com.example.vincent.shadcmobiletest.R
import com.example.vincent.shadcmobiletest.amazonaws.mobile.base.BaseActivity
import com.example.vincent.shadcmobiletest.amazonaws.mobile.model.ProductDO
import com.google.gson.Gson
import java.util.*


/**
 * Created by vincent on 30/03/2018.
 */

class NoSqlTestActivity : BaseActivity() {
    val EMPTY = 0
    val ONE = 1
    var dynamoDBMapper: DynamoDBMapper? = null
    var mTestGetFunctionBtn: Button? = null
    var mTestGetOneFunctionBtn: Button? = null
    var mTestCreateFunctionBtn: Button? = null
    var mTestUpdateFunctionBtn: Button? = null
    var mTestDeleteFunctionBtn: Button? = null
    var mLogOutBtn: Button? = null
    var mLogTv: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        TAG = NoSqlTestActivity::class.java.simpleName
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nosql)
        init()
        initListener()
    }

    fun init() {
        val dynamoDBClient = AmazonDynamoDBClient(AWSMobileClient.getInstance().credentialsProvider)
        this.dynamoDBMapper = DynamoDBMapper.builder()
                .dynamoDBClient(dynamoDBClient)
//                .awsConfiguration(awsconfig)
                .awsConfiguration(AWSMobileClient.getInstance().configuration)
                .build()
        mTestGetFunctionBtn = findViewById(R.id.test_api_btn)
        mTestGetOneFunctionBtn = findViewById<Button>(R.id.test_api_one_btn)
        mTestCreateFunctionBtn = findViewById<Button>(R.id.test_api_create_btn)
        mTestUpdateFunctionBtn = findViewById<Button>(R.id.test_api_update_btn)
        mTestDeleteFunctionBtn = findViewById<Button>(R.id.test_api_delete_btn)
        mLogOutBtn = findViewById<Button>(R.id.button2)
        mLogTv = findViewById(R.id.log_tv)

    }

    override fun dealWithHandlerMessage(msg: Message) {
        when (msg.what) {
            EMPTY -> mLogTv!!.setText("Query Result:" + msg.obj)
            ONE -> mLogTv!!.setText("product Item:" + msg.obj)

        }
    }

    fun initListener() {
        mLogOutBtn!!.setOnClickListener {
            AwsApplication.identityManager!!.signOut()
//            android.os.Process.killProcess(android.os.Process.myPid())
//            System.exit(1)
            finish()
        }
        mTestGetFunctionBtn!!.setOnClickListener {
            queryProducts()
        }
        mTestGetOneFunctionBtn!!.setOnClickListener {
            readNews()
        }
        mTestCreateFunctionBtn!!.setOnClickListener {
            createProduct()
        }
        mTestUpdateFunctionBtn!!.setOnClickListener {
            updateProduct()
        }
        mTestDeleteFunctionBtn!!.setOnClickListener {
            deleteNews()
        }
    }

    fun createProduct() {
        var productDO = ProductDO()
        productDO.date = 2.0
        productDO.description = "test product"
        productDO.price = 133.5
        productDO.name = "Vincent's product nosql"
        productDO.productId = UUID.randomUUID().toString().replace("-", "")
        Log.d(TAG, "productDO.productId:" + productDO.productId)

        Thread(Runnable {
            dynamoDBMapper!!.save(productDO)

            // Item saved
        }).start()
    }

    fun readNews() {
        Thread(Runnable {
            val productDao = dynamoDBMapper!!.load(
                    ProductDO::class.java,

                    // Use IdentityManager to get the user identity id.
                    AwsApplication.identityManager!!.cachedUserID,
                    "4b78c910ec12413c9011ec2bfea5c7b5")
            val message = Message.obtain()
            message.what = ONE
            if(productDao!=null){
                message.obj = productDao.getRequest()
                mHandler.sendMessage(message)
            } else{
                message.obj = productDao
                mHandler.sendMessage(message)
            }
        }).start()
    }

    fun updateProduct() {
        var productDO = ProductDO()
        productDO.description = "test product update"
        productDO.price = 22236.5
        productDO.productId = "4b78c910ec12413c9011ec2bfea5c7b5"
        Thread(Runnable {
            dynamoDBMapper!!.save(productDO)
            // Item updated
        }).start()
    }

    fun deleteNews() {
        Thread(Runnable {
            val productDO = ProductDO()
            productDO.productId = "4b78c910ec12413c9011ec2bfea5c7b5"
            dynamoDBMapper!!.delete(productDO)
            // Item deleted
        }).start()
    }

    fun queryProducts() {
        Thread(Runnable {
            // 1) Create a data object that maps to the News table
            val productDO = ProductDO()

            // 2) Configure the query

//            val rangeKeyCondition = Condition()
//                    .withComparisonOperator(ComparisonOperator.BEGINS_WITH)
//                    .withAttributeValueList(AttributeValue().withS("Trial"))
//
//
//            val filterExpressionAttributeNames = HashMap<String, String>()
//
//            filterExpressionAttributeNames.put("#author", "author")
//
//            val filterExpressionAttributeValues = HashMap<String, AttributeValue>()
//
//            filterExpressionAttributeValues.put(
//                    ":expectedName", AttributeValue().withS("minbi"))

            val queryExpression = DynamoDBQueryExpression<ProductDO>()
                    .withHashKeyValues(productDO)
//                    .withRangeKeyCondition("productId", rangeKeyCondition)
//                    .withFilterExpression("#author = :expectedName")
//                    .withExpressionAttributeNames(filterExpressionAttributeNames)
//                    .withExpressionAttributeValues(filterExpressionAttributeValues)
                    .withConsistentRead(false)

            // 3) Make the query

            val result = dynamoDBMapper!!.query(ProductDO::class.java, queryExpression)

            // 4) Use the result

            val gson = Gson()
            val stringBuilder = StringBuilder()

            for (i in result.indices) {
                val jsonFormOfItem = gson.toJson(result.get(i))
                stringBuilder.append(jsonFormOfItem + "\n\n")
            }

            Log.d(TAG, "Query Result:" + stringBuilder.toString())

            if (!result.isEmpty()) {
                val message = Message.obtain()
                message.what = EMPTY
                message.obj = stringBuilder.toString()
                mHandler.sendMessage(message)
                // No matching items
            }
            // Query successful
        }).start()
    }
}
