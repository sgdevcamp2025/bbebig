package com.smilegate.bbebig.data.api

import com.smilegate.bbebig.data.base.BaseResponse
import com.smilegate.bbebig.data.request.ServerCreateRequest
import com.smilegate.bbebig.data.response.ServerCreateResponse
import com.smilegate.bbebig.data.response.ServerInfoResponse
import com.smilegate.bbebig.data.response.ServerListResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ServerApiService {
    @GET("service-server/servers")
    suspend fun getServerList(): BaseResponse<ServerListResponse>

    @GET("service-server/servers{serverId}")
    suspend fun getServerInfo(@Path(value = "serverId") severId: Long): BaseResponse<ServerInfoResponse>

    @POST("/servers")
    suspend fun createServer(@Body serverRequest: ServerCreateRequest): BaseResponse<ServerCreateResponse>
}
