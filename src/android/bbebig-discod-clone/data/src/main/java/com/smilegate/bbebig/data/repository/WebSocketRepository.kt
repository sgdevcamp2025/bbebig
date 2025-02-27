package com.smilegate.bbebig.data.repository

import com.smilegate.bbebig.data.model.ChatDataModel
import kotlinx.coroutines.flow.Flow

interface WebSocketRepository {
    suspend fun connect(
        memberId: Long,
        roomType: String,
        channelId: Long,
        serverId: Long,
    )
    suspend fun disconnect()
    suspend fun sendMessage(memberId: Long, chatDataModel: ChatDataModel)
    suspend fun receiveMessage(serverId: Long, memberId: Long): Flow<ChatDataModel>
}
