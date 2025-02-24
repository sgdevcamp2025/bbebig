package com.smilegate.bbebig.presentation.ui.home.model

import com.smilegate.bbebig.domain.model.ServerDomainModel

data class Server(
    val serverId: Long,
    val serverName: String,
    val serverImageUrl: String?,
)

fun ServerDomainModel.toUiModel(): Server = Server(
    serverId = serverId.toLong(),
    serverName = serverName,
    serverImageUrl = serverImageUrl,
)

fun List<ServerDomainModel>.toUiModel(): List<Server> = map { it.toUiModel() }
