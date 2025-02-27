package com.smilegate.bbebig.data.datasource

import com.smilegate.bbebig.data.model.ChatMessageDataModel

interface SearchDataSource {
    suspend fun getChatHistory(serverId: Long, channelId: Long): ChatMessageDataModel
}
