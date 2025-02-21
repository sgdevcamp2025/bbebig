package com.smilegate.bbebig.data.datasource

import com.smilegate.bbebig.data.model.SignalMessageDataModel
import kotlinx.coroutines.flow.Flow
import org.hildan.krossbow.stomp.StompSession

interface WebSocketDataSource {
    val stompSession: StompSession
    suspend fun connect()
    suspend fun sendChannelStateGroupMessage(signalMessage: SignalMessageDataModel)
    suspend fun offerSdp(signalMessage: SignalMessageDataModel)
    suspend fun offerIceCandidate(signalMessage: SignalMessageDataModel)
    fun receiveChannelGroupMessage(channelId: String): Flow<SignalMessageDataModel>
    fun receiveUserGroupMessage(userId: String):Flow<SignalMessageDataModel>
}