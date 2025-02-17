package com.smilegate.bbebig.data.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.smilegate.bbebig.data.api.AuthApiService
import com.smilegate.bbebig.data.di.qulifier.AuthBaseUrl
import com.smilegate.bbebig.data.di.qulifier.AuthOkHttpClient
import com.smilegate.bbebig.data.di.qulifier.AuthRetrofit
import com.smilegate.bbebig.data.di.qulifier.TimeOutPolicy
import com.smilegate.bbebig.data.interceptor.AuthInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.create
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    @TimeOutPolicy
    fun provideConnectTimeoutPolicy(): Long {
        return 10_000L
    }

    @Singleton
    @Provides
    @AuthBaseUrl
    fun provideAuthBaseUrl(): String {
        return "http://43.203.136.82:8080"
    }

    @Singleton
    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Singleton
    @Provides
    fun provideJson(): Json {
        return Json {
            // Json 큰따옴표 느슨하게 체크.
            isLenient = true
            // Field 값이 없는 경우 무시
            ignoreUnknownKeys = true
            // "null" 이 들어간경우 default Argument 값으로 대체
            coerceInputValues = true
        }
    }

    @Singleton
    @Provides
    @AuthOkHttpClient
    fun provideAuthOkHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor,
        @TimeOutPolicy connectTimeoutPolicy: Long,
    ): OkHttpClient {
        return OkHttpClient
            .Builder()
            .addInterceptor(httpLoggingInterceptor)
            .connectTimeout(
                connectTimeoutPolicy,
                TimeUnit.MILLISECONDS,
            )
            .build()
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(
        authInterceptor: AuthInterceptor,
        httpLoggingInterceptor: HttpLoggingInterceptor,
        @TimeOutPolicy connectTimeoutPolicy: Long,
    ): OkHttpClient {
        return OkHttpClient
            .Builder()
            .addInterceptor(httpLoggingInterceptor)
            .addInterceptor(authInterceptor)
            .connectTimeout(
                connectTimeoutPolicy,
                TimeUnit.MILLISECONDS,
            )
            .build()
    }

    @Singleton
    @Provides
    @AuthRetrofit
    fun provideAuthRetrofit(
        @AuthOkHttpClient okHttpClient: OkHttpClient,
        json: Json,
        @AuthBaseUrl baseUrl: String,
    ): Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(okHttpClient)
        .addConverterFactory(
            json.asConverterFactory("application/json".toMediaType()),
        )
        .build()

    @Singleton
    @Provides
    fun provideAuthService(
        @AuthRetrofit retrofit: Retrofit,
    ): AuthApiService = retrofit.create()
}
