package com.smilegate.bbebig.data.repository

import com.smilegate.bbebig.data.datasource.ServerDataSource
import com.smilegate.bbebig.data.model.ServerDataModel
import com.smilegate.bbebig.data.model.ServerInfoDataModel
import javax.inject.Inject

class ServerRepositoryImpl @Inject constructor(
    private val serverDataSource: ServerDataSource,
) : ServerRepository {
    override suspend fun getServerList(): List<ServerDataModel> {
        return serverDataSource.getServerList()
    }

    override suspend fun getServerInfo(serverId: Long): ServerInfoDataModel {
        return serverDataSource.getServerInfo(serverId)
    }
}
