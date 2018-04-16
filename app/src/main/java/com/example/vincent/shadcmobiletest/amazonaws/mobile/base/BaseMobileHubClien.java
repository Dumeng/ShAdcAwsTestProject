package com.example.vincent.shadcmobiletest.amazonaws.mobile.base;

import com.amazonaws.mobileconnectors.apigateway.ApiRequest;
import com.amazonaws.mobileconnectors.apigateway.ApiResponse;
import com.amazonaws.mobileconnectors.apigateway.annotation.Service;

/**
 * Created by vincent on 08/04/2018.
 */
@Service(endpoint = "")
public interface BaseMobileHubClien {
    /**
     * A generic invoker to invoke any API Gateway endpoint.
     *
     * @param request
     * @return ApiResponse
     */
    ApiResponse execute(ApiRequest request);
}
