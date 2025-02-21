package com.smilegate.bbebig.data.datasource

import com.smilegate.bbebig.data.model.ServerCreateDataModel
import com.smilegate.bbebig.data.model.ServerDataModel
import com.smilegate.bbebig.data.model.ServerInfoDataModel
import com.smilegate.bbebig.data.request.ServerCreateRequest

interface ServerDataSource {
    suspend fun getServerList(): List<ServerDataModel>
    suspend fun getServerInfo(serverId: Long): ServerInfoDataModel
    suspend fun createServer(serverCreateRequest: ServerCreateRequest): ServerCreateDataModel
}
