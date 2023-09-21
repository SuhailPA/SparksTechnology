package com.example.sparktech.utils

import okhttp3.Interceptor
import okhttp3.Response

class OAuthInterceptor constructor(
    private val tokenType: String,
    private val accessToken: String?
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val newRequest = if (!accessToken.isNullOrEmpty()) {
            request.newBuilder().header("Authorization", "$tokenType $accessToken")
                .build()
        } else {
            request
        }
        return chain.proceed(newRequest)
    }
}