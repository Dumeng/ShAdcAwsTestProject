package com.example.vincent.shadcmobiletest.amazonaws.mobile.model

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAttribute
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBHashKey
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBRangeKey
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable
import com.example.vincent.shadcmobiletest.amazonaws.mobile.apiClient.IRequestParams
import com.google.gson.Gson
import java.io.Serializable
import com.example.vincent.shadcmobiletest.AwsApplication

@DynamoDBTable(tableName = "adctest-mobilehub-487482852-product")
class ProductDO : Serializable, IRequestParams() {
    @get:DynamoDBHashKey(attributeName = "userId")
    @get:DynamoDBAttribute(attributeName = "userId")
    var userId = AwsApplication.identityManager!!.cachedUserID
    @get:DynamoDBRangeKey(attributeName = "productId")
    @get:DynamoDBAttribute(attributeName = "productId")
    var productId: String? = null
    @get:DynamoDBAttribute(attributeName = "date")
    var date: Double? = null
    @get:DynamoDBAttribute(attributeName = "description")
    var description: String? = null
    @get:DynamoDBAttribute(attributeName = "name")
    var name: String? = null
    @get:DynamoDBAttribute(attributeName = "price")
    var price: Double? = null
    override fun getPrintableRequest(gson: Gson): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

//    override fun getRequest(): String {
//        TODO("not implemented") //
//        return Gson().toJson(this)
//// To change body of created functions use File | Settings | File Templates.
//    }
}
