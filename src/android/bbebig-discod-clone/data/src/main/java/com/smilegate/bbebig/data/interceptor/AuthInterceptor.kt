package com.smilegate.bbebig.data.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthInterceptor @Inject constructor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        return chain.proceed(
            chain.request()
                .newBuilder()
                .addHeader("Authorization", "Bearer $TOKEN")
                .build(),
        )
    }

    companion object {
        private const val TOKEN = ""
    }
}
