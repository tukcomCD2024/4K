package com.example.front_end_android.util

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val accessToken: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("access-token", accessToken)
            .build()
        return chain.proceed(request)
    }
}