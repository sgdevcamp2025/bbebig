package com.smilegate.bbebig.data.repository

import com.smilegate.bbebig.data.model.ChatMessageDataModel

interface SearchRepository {
    suspend fun getChatHistory(serverId: Long, channelId: Long): ChatMessageDataModel
}
