package com.smilegate.bbebig.data.repository

import com.smilegate.bbebig.data.model.SignalMessageDataModel
import kotlinx.coroutines.flow.Flow
import org.hildan.krossbow.stomp.StompSession

interface WebSocketRepository {
    //suspend fun connect(): StompSession
    suspend fun sendChannelStateGroupMessage(signalMessage: SignalMessageDataModel)
    fun receiveChannelGroupMessage(channelId: String): Flow<SignalMessageDataModel>
    fun receiveUserGroupMessage(userId: String): Flow<SignalMessageDataModel>
}