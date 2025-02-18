package com.smilegate.bbebig.data.repository

import com.smilegate.bbebig.data.model.ServerDataModel

interface ServerRepository {
    suspend fun getServerList(): List<ServerDataModel>
}
