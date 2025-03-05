package com.smilegate.bbebig.data.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.smilegate.bbebig.data.api.AuthApiService
import com.smilegate.bbebig.data.api.SearchApiService
import com.smilegate.bbebig.data.api.ServerApiService
import com.smilegate.bbebig.data.api.UserApiService
import com.smilegate.bbebig.data.di.qulifier.AuthOkHttpClient
import com.smilegate.bbebig.data.di.qulifier.AuthRetrofit
import com.smilegate.bbebig.data.di.qulifier.BaseUrl
import com.smilegate.bbebig.data.di.qulifier.ChatWebSocketUrl
import com.smilegate.bbebig.data.di.qulifier.DefaultOkHttpClient
import com.smilegate.bbebig.data.di.qulifier.ServerRetrofit
import com.smilegate.bbebig.data.di.qulifier.StompWebSocketClient
import com.smilegate.bbebig.data.di.qulifier.TimeOutPolicy
import com.smilegate.bbebig.data.di.qulifier.WebSocketClient
import com.smilegate.bbebig.data.interceptor.AuthInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.hildan.krossbow.stomp.StompClient
import org.hildan.krossbow.websocket.okhttp.OkHttpWebSocketClient
import retrofit2.Retrofit
import retrofit2.create
import java.time.Duration
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
    @BaseUrl
    fun provideBaseUrl(): String {
        return "http://43.203.136.82:8080"
    }

    @Singleton
    @Provides
    @ChatWebSocketUrl
    fun provideChatWebSocketUrl(): String {
        return "wss://bbebig.store/ws-mobile"
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
    @WebSocketClient
    fun provideWebSocketClient(
        httpLoggingInterceptor: HttpLoggingInterceptor,
        authInterceptor: AuthInterceptor,
    ): OkHttpClient {
        return OkHttpClient
            .Builder()
            .addInterceptor(httpLoggingInterceptor)
            .addInterceptor(authInterceptor)
            .callTimeout(Duration.ofMinutes(1))
            .pingInterval(Duration.ofSeconds(10))
            .retryOnConnectionFailure(true)
            .build()
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
    @DefaultOkHttpClient
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
        @BaseUrl baseUrl: String,
    ): Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(okHttpClient)
        .addConverterFactory(
            json.asConverterFactory("application/json".toMediaType()),
        )
        .build()

    @Singleton
    @Provides
    @ServerRetrofit
    fun provideServerRetrofit(
        @DefaultOkHttpClient okHttpClient: OkHttpClient,
        json: Json,
        @BaseUrl baseUrl: String,
    ): Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(okHttpClient)
        .addConverterFactory(
            json.asConverterFactory("application/json".toMediaType()),
        )
        .build()

    @Singleton
    @Provides
    @StompWebSocketClient
    fun provideStompClient(
        @WebSocketClient webSocketClient: OkHttpClient,
    ): StompClient {
        return StompClient(OkHttpWebSocketClient(webSocketClient))
    }

    @Singleton
    @Provides
    fun provideAuthService(
        @AuthRetrofit retrofit: Retrofit,
    ): AuthApiService = retrofit.create()

    @Singleton
    @Provides
    fun provideServerService(
        @ServerRetrofit retrofit: Retrofit,
    ): ServerApiService = retrofit.create()

    @Singleton
    @Provides
    fun provideUserService(
        @ServerRetrofit retrofit: Retrofit,
    ): UserApiService = retrofit.create()

    @Singleton
    @Provides
    fun provideChatService(
        @ServerRetrofit retrofit: Retrofit,
    ): SearchApiService = retrofit.create()
}
