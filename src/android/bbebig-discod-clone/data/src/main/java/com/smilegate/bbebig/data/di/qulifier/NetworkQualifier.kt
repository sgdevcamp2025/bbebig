package com.smilegate.bbebig.data.di.qulifier

import jakarta.inject.Qualifier

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class BaseUrl

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class TimeOutPolicy

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class AuthRetrofit

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class AuthOkHttpClient

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class DefaultOkHttpClient

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class ServerRetrofit
