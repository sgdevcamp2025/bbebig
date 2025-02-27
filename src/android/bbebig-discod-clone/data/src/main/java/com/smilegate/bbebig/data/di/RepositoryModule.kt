package com.smilegate.bbebig.data.di

import com.smilegate.bbebig.data.repository.AuthRepository
import com.smilegate.bbebig.data.repository.AuthRepositoryImpl
import com.smilegate.bbebig.data.repository.SearchRepository
import com.smilegate.bbebig.data.repository.SearchRepositoryImpl
import com.smilegate.bbebig.data.repository.ServerRepository
import com.smilegate.bbebig.data.repository.ServerRepositoryImpl
import com.smilegate.bbebig.data.repository.UserRepository
import com.smilegate.bbebig.data.repository.UserRepositoryImpl
import com.smilegate.bbebig.data.repository.WebSocketRepository
import com.smilegate.bbebig.data.repository.WebSocketRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    @Singleton
    fun bindAuthRepository(authRepositoryImpl: AuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    fun bindServerRepository(serverRepositoryImpl: ServerRepositoryImpl): ServerRepository

    @Binds
    @Singleton
    fun bindUserRepository(userRepositoryImpl: UserRepositoryImpl): UserRepository

    @Binds
    @Singleton
    fun bindWebSocketRepository(webSocketRepositoryImpl: WebSocketRepositoryImpl): WebSocketRepository

    @Binds
    @Singleton
    fun bindSearchRepository(searchRepositoryImpl: SearchRepositoryImpl): SearchRepository
}
