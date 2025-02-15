package com.smilegate.bbebig.data.repository

import com.smilegate.bbebig.data.datasource.AuthDataSource
import com.smilegate.bbebig.data.model.SignUpDataModel
import com.smilegate.bbebig.data.request.SignUpRequest
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authDataSource: AuthDataSource,
) : AuthRepository {
    override suspend fun signUp(signUpRequest: SignUpRequest): SignUpDataModel {
        return authDataSource.signUp(signUpRequest)
    }
}
