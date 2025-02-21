package com.smilegate.bbebig.data.repository

import com.smilegate.bbebig.data.model.ServerDataModel
import com.smilegate.bbebig.data.model.ServerInfoDataModel

interface ServerRepository {
    suspend fun getServerList(): List<ServerDataModel>
    suspend fun getServerInfo(serverId: Long): ServerInfoDataModel
}
