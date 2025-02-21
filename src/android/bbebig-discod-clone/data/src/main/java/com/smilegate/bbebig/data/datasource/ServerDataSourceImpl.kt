package com.smilegate.bbebig.data.datasource

import com.smilegate.bbebig.data.api.ServerApiService
import com.smilegate.bbebig.data.base.callApi
import com.smilegate.bbebig.data.model.ServerCreateDataModel
import com.smilegate.bbebig.data.model.ServerDataModel
import com.smilegate.bbebig.data.model.ServerInfoDataModel
import com.smilegate.bbebig.data.model.ServerJoinDataModel
import com.smilegate.bbebig.data.model.toDataModel
import com.smilegate.bbebig.data.request.ServerCreateRequest
import javax.inject.Inject

class ServerDataSourceImpl @Inject constructor(
    private val serverService: ServerApiService,
) : ServerDataSource {
    override suspend fun getServerList(): List<ServerDataModel> {
        return callApi { serverService.getServerList() }.servers.toDataModel()
    }

    override suspend fun getServerInfo(serverId: Long): ServerInfoDataModel {
        return callApi { serverService.getServerInfo(serverId) }.toDataModel()
    }

    override suspend fun createServer(serverCreateRequest: ServerCreateRequest): ServerCreateDataModel {
        return callApi { serverService.createServer(serverCreateRequest) }.toDataModel()
    }

    override suspend fun joinServer(serverId: Long): ServerJoinDataModel {
        return callApi { serverService.joinServer(serverId) }.toDataModel()
    }
}
