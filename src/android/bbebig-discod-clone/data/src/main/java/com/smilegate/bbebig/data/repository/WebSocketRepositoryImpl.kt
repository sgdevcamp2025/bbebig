package com.smilegate.bbebig.data.repository

import com.smilegate.bbebig.data.datasource.WebSocketDataSource
import com.smilegate.bbebig.data.model.ChatDataModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WebSocketRepositoryImpl @Inject constructor(
    private val webSocketDataSource: WebSocketDataSource,
) : WebSocketRepository {
    override suspend fun connect(
        memberId: Long,
        roomType: String,
        channelId: Long,
        serverId: Long,
    ) {
        webSocketDataSource.connect(
            memberId,
            roomType,
            channelId,
            serverId,
        )
    }

    override suspend fun disconnect() {
        return webSocketDataSource.disconnect()
    }

    override suspend fun sendMessage(memberId: Long, chatDataModel: ChatDataModel) {
        return webSocketDataSource.sendMessage(memberId, chatDataModel)
    }

    override suspend fun receiveMessage(serverId: Long, memberId: Long): Flow<ChatDataModel> {
        return webSocketDataSource.receiveMessage(serverId, memberId)
    }
}
