package com.upt.cleancity.service.common

import com.upt.cleancity.utils.AppState
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val response = chain.proceed(original)

        val authToken = "Bearer " + AppState.currentToken.accessToken

        val request = original.newBuilder()
            .header("Authorization", authToken)
            .method(original.method, original.body).build()
        return chain.proceed(request)
    }
}