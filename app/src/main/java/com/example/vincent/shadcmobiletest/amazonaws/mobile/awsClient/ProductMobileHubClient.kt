/*
 * Copyright 2010-2016 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 *  http://aws.amazon.com/apache2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package com.example.vincent.shadcmobiletest.amazonaws.mobile.awsClient

import com.amazonaws.mobileconnectors.apigateway.ApiRequest
import com.amazonaws.mobileconnectors.apigateway.ApiResponse
import com.amazonaws.mobileconnectors.apigateway.annotation.Service
import com.example.vincent.shadcmobiletest.amazonaws.mobile.base.BaseMobileHubClien

@Service(endpoint = "https://yvnccrnav6.execute-api.us-east-1.amazonaws.com/Development")
interface ProductMobileHubClient : BaseMobileHubClien {


    /**
     * A generic invoker to invoke any API Gateway endpoint.
     *
     * @param request
     * @return ApiResponse
     */
    override fun execute(request: ApiRequest): ApiResponse
}

