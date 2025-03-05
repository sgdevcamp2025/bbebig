package com.smilegate.bbebig.domain.model

import com.smilegate.bbebig.data.model.ServerDataModel

data class ServerDomainModel(
    val serverId: Int,
    val serverName: String,
    val serverImageUrl: String?,
)

fun ServerDataModel.toDomainModel(): ServerDomainModel = ServerDomainModel(
    serverId = serverId,
    serverName = serverName,
    serverImageUrl = serverImageUrl,
)

fun List<ServerDataModel>.toDomainModel(): List<ServerDomainModel> = map { it.toDomainModel() }
