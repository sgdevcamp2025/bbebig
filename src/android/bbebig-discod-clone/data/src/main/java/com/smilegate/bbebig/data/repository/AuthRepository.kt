package com.smilegate.bbebig.data.repository

import com.smilegate.bbebig.data.model.LoginDataModel
import com.smilegate.bbebig.data.model.LoginInfoDataModel
import com.smilegate.bbebig.data.model.SignUpDataModel
import com.smilegate.bbebig.data.request.LoginRequest
import com.smilegate.bbebig.data.request.SignUpRequest

interface AuthRepository {
    suspend fun signUp(signUpRequest: SignUpRequest): SignUpDataModel
    suspend fun login(loginRequest: LoginRequest): LoginDataModel
    fun saveToken(accessToken: String, refreshToken: String)
    fun getAccessToken(): String?
    fun getRefreshToken(): String?
    fun saveLoginInfo(loginInfo: LoginInfoDataModel)
    fun loadLoginInfo(): LoginInfoDataModel
}
