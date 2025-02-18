package com.smilegate.bbebig.data.datasource

import com.smilegate.bbebig.data.api.ServerApiService
import com.smilegate.bbebig.data.base.callApi
import com.smilegate.bbebig.data.model.ServerDataModel
import com.smilegate.bbebig.data.model.toDataModel
import javax.inject.Inject

class ServerDataSourceImpl @Inject constructor(
    private val serverService: ServerApiService,
) : ServerDataSource {
    override suspend fun getServerList(): List<ServerDataModel> {
        return callApi { serverService.getServerList() }.servers.toDataModel()
    }
}
