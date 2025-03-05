package com.smilegate.bbebig.data.interceptor

import com.smilegate.bbebig.data.datasource.LocalDataSource
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthInterceptor @Inject constructor(
    private val localDataSource: LocalDataSource,
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        return chain.proceed(
            chain.request()
                .newBuilder()
                .addHeader("Authorization", "Bearer ${localDataSource.getAccessToken()})")
                .build(),
        )
    }
}
