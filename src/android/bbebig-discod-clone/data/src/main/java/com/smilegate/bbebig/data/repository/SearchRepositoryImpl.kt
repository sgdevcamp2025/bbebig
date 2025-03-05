package com.smilegate.bbebig.data.repository

import com.smilegate.bbebig.data.datasource.SearchDataSource
import com.smilegate.bbebig.data.model.ChatMessageDataModel
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val searchDataSource: SearchDataSource,
) : SearchRepository {
    override suspend fun getChatHistory(serverId: Long, channelId: Long): ChatMessageDataModel {
        return searchDataSource.getChatHistory(serverId, channelId)
    }
}
