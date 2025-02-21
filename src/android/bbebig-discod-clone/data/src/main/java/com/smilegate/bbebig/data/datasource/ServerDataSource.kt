package com.smilegate.bbebig.data.datasource

import com.smilegate.bbebig.data.model.ServerDataModel
import com.smilegate.bbebig.data.model.ServerInfoDataModel

interface ServerDataSource {
    suspend fun getServerList(): List<ServerDataModel>
    suspend fun getServerInfo(serverId: Long): ServerInfoDataModel
}
