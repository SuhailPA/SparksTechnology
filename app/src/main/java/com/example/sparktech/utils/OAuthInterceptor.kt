package com.example.sparktech.utils

import android.content.SharedPreferences
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class OAuthInterceptor @Inject constructor(val encryptedPref : SharedPreferences): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val accessToken = encryptedPref.getString("accessToken","")
        var request = chain.request()
        val newRequest = if (!accessToken.isNullOrEmpty()) {
            request.newBuilder().header("Authorization", "Bearer $accessToken")
                .build()
        } else {
            request
        }
        return chain.proceed(newRequest)
    }
}