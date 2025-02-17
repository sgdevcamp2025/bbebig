package com.smilegate.bbebig.data.repository

import com.smilegate.bbebig.data.datasource.AuthDataSource
import com.smilegate.bbebig.data.datasource.LocalDataSource
import com.smilegate.bbebig.data.model.LoginDataModel
import com.smilegate.bbebig.data.model.LoginInfoDataModel
import com.smilegate.bbebig.data.model.SignUpDataModel
import com.smilegate.bbebig.data.request.LoginRequest
import com.smilegate.bbebig.data.request.SignUpRequest
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authDataSource: AuthDataSource,
    private val localDataSource: LocalDataSource,
) : AuthRepository {
    override suspend fun signUp(signUpRequest: SignUpRequest): SignUpDataModel {
        return authDataSource.signUp(signUpRequest)
    }

    override suspend fun login(loginRequest: LoginRequest): LoginDataModel {
        return authDataSource.login(loginRequest)
    }

    override fun saveToken(accessToken: String, refreshToken: String) {
        localDataSource.saveToken(accessToken, refreshToken)
    }

    override fun getAccessToken(): String? {
        return localDataSource.getAccessToken()
    }

    override fun getRefreshToken(): String? {
        return localDataSource.getRefreshToken()
    }

    override fun saveLoginInfo(loginInfo: LoginInfoDataModel) {
        localDataSource.saveLoginInfo(loginInfo)
    }

    override fun loadLoginInfo(): LoginInfoDataModel {
        return localDataSource.loadLoginInfo()
    }
}
