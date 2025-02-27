package com.smilegate.bbebig.data.datasource

import com.smilegate.bbebig.data.model.ChatDataModel
import kotlinx.coroutines.flow.Flow
import org.hildan.krossbow.stomp.StompSession

interface WebSocketDataSource {
    var stompSession: StompSession
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
