package com.smilegate.bbebig.data.repository

import com.smilegate.bbebig.data.model.ServerCreateDataModel
import com.smilegate.bbebig.data.model.ServerDataModel
import com.smilegate.bbebig.data.model.ServerInfoDataModel
import com.smilegate.bbebig.data.model.ServerJoinDataModel
import com.smilegate.bbebig.data.request.ServerCreateRequest

interface ServerRepository {
    suspend fun getServerList(): List<ServerDataModel>
    suspend fun getServerInfo(serverId: Long): ServerInfoDataModel
    suspend fun createServer(serverCreateRequest: ServerCreateRequest): ServerCreateDataModel
    suspend fun joinServer(serverId: Long): ServerJoinDataModel
}
