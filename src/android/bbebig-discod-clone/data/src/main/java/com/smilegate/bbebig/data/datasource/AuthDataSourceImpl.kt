package com.smilegate.bbebig.data.datasource

import com.smilegate.bbebig.data.api.AuthApiService
import com.smilegate.bbebig.data.base.callApi
import com.smilegate.bbebig.data.model.LoginDataModel
import com.smilegate.bbebig.data.model.SignUpDataModel
import com.smilegate.bbebig.data.model.toDataModel
import com.smilegate.bbebig.data.request.LoginRequest
import com.smilegate.bbebig.data.request.SignUpRequest
import javax.inject.Inject

class AuthDataSourceImpl @Inject constructor(
    private val authApiService: AuthApiService,
) : AuthDataSource {
    override suspend fun signUp(request: SignUpRequest): SignUpDataModel {
        return authApiService.signUp(request).toDataModel()
    }

    override suspend fun login(request: LoginRequest): LoginDataModel {
        return callApi { authApiService.login(request) }.toDataModel()
    }
}
