package com.smilegate.bbebig.presentation.ui.home.model

import com.smilegate.bbebig.domain.model.ServerInfoDomainModel

data class ServerInfo(
    val categoryInfoList: List<CategoryInfo>,
    val channelInfoList: List<ChannelInfo>,
    val ownerId: Long,
    val serverId: Long,
    val serverImageUrl: String,
    val serverName: String,
)

fun ServerInfoDomainModel.toUiModel(): ServerInfo = ServerInfo(
    categoryInfoList = categoryInfoList.map { it.toUiModel() },
    channelInfoList = channelInfoList.map { it.toUiModel() },
    ownerId = ownerId,
    serverId = serverId,
    serverImageUrl = serverImageUrl,
    serverName = serverName,
)
