package com.smilegate.bbebig.data.di

import com.smilegate.bbebig.data.datasource.AuthDataSource
import com.smilegate.bbebig.data.datasource.AuthDataSourceImpl
import com.smilegate.bbebig.data.datasource.LocalDataSource
import com.smilegate.bbebig.data.datasource.LocalDataSourceImpl
import com.smilegate.bbebig.data.datasource.ServerDataSource
import com.smilegate.bbebig.data.datasource.ServerDataSourceImpl
import com.smilegate.bbebig.data.datasource.WebRTCDataSource
import com.smilegate.bbebig.data.datasource.WebRTCDataSourceImpl
import com.smilegate.bbebig.data.datasource.WebSocketDataSource
import com.smilegate.bbebig.data.datasource.WebSocketDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataSourceModule {

    @Binds
    @Singleton
    fun bindAuthDataSource(authDataSourceImpl: AuthDataSourceImpl): AuthDataSource

    @Binds
    @Singleton
    fun bindLocalDataSource(localDataSourceImpl: LocalDataSourceImpl): LocalDataSource

    @Binds
    @Singleton
    fun bindServerDataSource(serverDataSourceImpl: ServerDataSourceImpl): ServerDataSource

    @Binds
    @Singleton
    fun bindWebSocketDataSource(webSocketDataSourceImpl: WebSocketDataSourceImpl): WebSocketDataSource

    @Binds
    @Singleton
    fun bindWebRTCDataSource(webRTCDataSourceImpl: WebRTCDataSourceImpl): WebRTCDataSource
}
