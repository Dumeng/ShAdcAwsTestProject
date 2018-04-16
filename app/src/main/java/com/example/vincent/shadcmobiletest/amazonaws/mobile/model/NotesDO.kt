package com.example.vincent.shadcmobiletest.amazonaws.mobile.model

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAttribute
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBHashKey
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBIndexHashKey
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBIndexRangeKey
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBRangeKey
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable

@DynamoDBTable(tableName = "adctest-mobilehub-487482852-Notes")
class NotesDO {
    @get:DynamoDBHashKey(attributeName = "userId")
    @get:DynamoDBIndexHashKey(attributeName = "userId", globalSecondaryIndexName = "DateSorted")
    var userId: String? = null
    @get:DynamoDBRangeKey(attributeName = "noteId")
    @get:DynamoDBAttribute(attributeName = "noteId")
    var noteId: String? = null
    @get:DynamoDBAttribute(attributeName = "content")
    var content: String? = null
    @get:DynamoDBIndexRangeKey(attributeName = "creationDate", globalSecondaryIndexName = "DateSorted")
    var creationDate: Double? = null
    @get:DynamoDBAttribute(attributeName = "title")
    var title: String? = null

}
