package com.smilegate.bbebig.data.repository

import com.smilegate.bbebig.data.model.SignUpDataModel
import com.smilegate.bbebig.data.request.SignUpRequest

interface AuthRepository {
    suspend fun signUp(signUpRequest: SignUpRequest): SignUpDataModel
}
