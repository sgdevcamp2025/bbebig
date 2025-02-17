package com.smilegate.bbebig.data.datasource

import com.smilegate.bbebig.data.model.LoginDataModel
import com.smilegate.bbebig.data.model.SignUpDataModel
import com.smilegate.bbebig.data.request.LoginRequest
import com.smilegate.bbebig.data.request.SignUpRequest

interface AuthDataSource {
    suspend fun signUp(request: SignUpRequest): SignUpDataModel
    suspend fun login(request: LoginRequest): LoginDataModel
}
