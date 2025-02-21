package com.smilegate.bbebig.data.repository

import com.smilegate.bbebig.data.datasource.WebSocketDataSource
import com.smilegate.bbebig.data.model.SignalMessageDataModel
import kotlinx.coroutines.flow.Flow
import org.hildan.krossbow.stomp.StompSession
import javax.inject.Inject

class WebSocketRepositoryImpl @Inject constructor(
    private val webSocketDataSource: WebSocketDataSource,
) : WebSocketRepository {
//    override suspend fun connect(): StompSession {
//        return webSocketDataSource.connect()
//    }

    override suspend fun sendChannelStateGroupMessage(signalMessage: SignalMessageDataModel) {
        webSocketDataSource.sendChannelStateGroupMessage(signalMessage)
    }

    override fun receiveChannelGroupMessage(channelId: String): Flow<SignalMessageDataModel> {
        return webSocketDataSource.receiveChannelGroupMessage(channelId)
    }

    override fun receiveUserGroupMessage(userId: String): Flow<SignalMessageDataModel> {
        return webSocketDataSource.receiveUserGroupMessage(userId)
    }
}