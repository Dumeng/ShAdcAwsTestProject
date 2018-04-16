package com.example.vincent.shadcmobiletest.amazonaws.mobile.apiClient

import com.google.gson.Gson

/**
 * Created by vincent on 04/04/2018.
 */

abstract class IRequestParams {
    /** Return a json string for this request  */
//    fun getRequest(gson: Gson): String

    open fun getRequest(): String {
        return Gson().toJson(this)
    }

    /** Return a json string that can be logged out
     * (No passwords or some other sensitive information!)  */
    abstract fun getPrintableRequest(gson: Gson): String
}
