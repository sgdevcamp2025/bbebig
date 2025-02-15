package com.smilegate.bbebig.data.datasource

import android.util.Log
import com.smilegate.bbebig.data.api.AuthApiService
import com.smilegate.bbebig.data.model.SignUpDataModel
import com.smilegate.bbebig.data.model.toDataModel
import com.smilegate.bbebig.data.request.SignUpRequest
import javax.inject.Inject

class AuthDataSourceImpl @Inject constructor(
    private val authApiService: AuthApiService,
) : AuthDataSource {
    override suspend fun signUp(request: SignUpRequest): SignUpDataModel {
        return authApiService.signUp(request).toDataModel().also { Log.d("SignUpDataModel", it.toString()) }
    }
}
