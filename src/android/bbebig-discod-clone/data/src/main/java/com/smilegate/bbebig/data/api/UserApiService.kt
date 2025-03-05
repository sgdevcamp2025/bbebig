package com.smilegate.bbebig.data.api

import com.smilegate.bbebig.data.base.BaseResponse
import com.smilegate.bbebig.data.response.MemberInfoResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface UserApiService {
    @GET("user-server/members/{memberId}")
    suspend fun getUserInfo(@Path("memberId") memberId: Long): BaseResponse<MemberInfoResponse>

    @GET("user-server/members/self")
    suspend fun getSelfInfo(): BaseResponse<MemberInfoResponse>

    @GET("user-server/members/search")
    suspend fun searchUser(@Query("nickName") keyword: String): BaseResponse<MemberInfoResponse>
}
