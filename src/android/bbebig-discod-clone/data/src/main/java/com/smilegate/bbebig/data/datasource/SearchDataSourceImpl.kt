package com.smilegate.bbebig.data.datasource

import com.smilegate.bbebig.data.api.SearchApiService
import com.smilegate.bbebig.data.base.callApi
import com.smilegate.bbebig.data.model.ChatMessageDataModel
import com.smilegate.bbebig.data.model.toDataModel
import javax.inject.Inject

class SearchDataSourceImpl @Inject constructor(
    private val searchApiService: SearchApiService,
) : SearchDataSource {
    override suspend fun getChatHistory(serverId: Long, channelId: Long): ChatMessageDataModel {
        return callApi { searchApiService.getChatHistoryMessages(serverId, channelId) }.toDataModel()
    }
}
