package com.smilegate.bbebig.data.di.qulifier

import jakarta.inject.Qualifier

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class AuthBaseUrl

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class TimeOutPolicy

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class AuthRetrofit

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class AuthOkHttpClient
