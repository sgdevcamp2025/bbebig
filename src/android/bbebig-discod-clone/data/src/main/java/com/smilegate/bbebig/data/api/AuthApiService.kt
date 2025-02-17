package com.smilegate.bbebig.data.api

import com.smilegate.bbebig.data.base.BaseResponse
import com.smilegate.bbebig.data.request.LoginRequest
import com.smilegate.bbebig.data.request.SignUpRequest
import com.smilegate.bbebig.data.response.LoginResponse
import com.smilegate.bbebig.data.response.SignUpResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {
    @POST("auth-server/auth/register")
    suspend fun signUp(
        @Body signUpRequest: SignUpRequest,
    ): SignUpResponse

    @POST("auth-server/auth/mobile-login")
    suspend fun login(
        @Body loginRequest: LoginRequest,
    ): BaseResponse<LoginResponse>
}
