package com.smilegate.bbebig.data.model

import com.smilegate.bbebig.data.response.ServerResponse

data class ServerDataModel(
    val serverId: Int,
    val serverName: String,
    val serverImageUrl: String?,
)

fun ServerResponse.toDataModel(): ServerDataModel = ServerDataModel(
    serverId = serverId,
    serverName = serverName,
    serverImageUrl = serverImageUrl,
)

fun List<ServerResponse>.toDataModel(): List<ServerDataModel> = map { it.toDataModel() }
