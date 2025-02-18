package com.smilegate.bbebig.data.datasource

import com.smilegate.bbebig.data.model.ServerDataModel

interface ServerDataSource {
    suspend fun getServerList(): List<ServerDataModel>
}
