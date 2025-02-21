package com.smilegate.bbebig.data.api

import com.smilegate.bbebig.data.base.BaseResponse
import com.smilegate.bbebig.data.response.ServerInfoResponse
import com.smilegate.bbebig.data.response.ServerListResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface ServerApiService {
    @GET("service-server/servers")
    suspend fun getServerList(): BaseResponse<ServerListResponse>

    @GET("service-server/servers{serverId}")
    suspend fun getServerInfo(@Path(value = "serverId") severId: Long): BaseResponse<ServerInfoResponse>
}
