package com.smilegate.bbebig.data.api

import com.smilegate.bbebig.data.base.BaseResponse
import com.smilegate.bbebig.data.response.ChatMessageResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface SearchApiService {
    @GET("search-server/history/server/{serverId}/channel/{channelId}/messages")
    suspend fun getChatHistoryMessages(
        @Path(value = "serverId") serverId: Long,
        @Path(value = "channelId") channelId: Long,
    ): BaseResponse<ChatMessageResponse>
}
