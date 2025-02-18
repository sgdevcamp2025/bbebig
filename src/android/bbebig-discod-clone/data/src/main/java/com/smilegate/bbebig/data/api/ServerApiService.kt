package com.smilegate.bbebig.data.api

import com.smilegate.bbebig.data.base.BaseResponse
import com.smilegate.bbebig.data.response.ServerListResponse
import retrofit2.http.GET

interface ServerApiService {
    @GET("service-server/servers")
    suspend fun getServerList(): BaseResponse<ServerListResponse>
}
