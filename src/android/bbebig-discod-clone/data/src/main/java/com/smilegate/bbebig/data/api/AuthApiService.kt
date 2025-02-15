package com.smilegate.bbebig.data.api

import com.smilegate.bbebig.data.request.SignUpRequest
import com.smilegate.bbebig.data.response.SignUpResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {
    @POST("auth-server/register")
    suspend fun signUp(
        @Body signUpRequest: SignUpRequest,
    ): SignUpResponse
}
