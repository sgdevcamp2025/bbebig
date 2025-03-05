package com.smilegate.bbebig.data.datasource

import com.smilegate.bbebig.data.model.LoginInfoDataModel

interface LocalDataSource {
    fun removeAll()
    fun saveToken(accessToken: String, refreshToken: String)
    fun removeToken()
    fun getAccessToken(): String?
    fun getRefreshToken(): String?
    fun loadLoginInfo(): LoginInfoDataModel
    fun saveLoginInfo(loginInfo: LoginInfoDataModel)
}
