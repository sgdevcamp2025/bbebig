package com.smilegate.bbebig.domain.model

import com.smilegate.bbebig.data.model.ServerInfoDataModel

data class ServerInfoDomainModel(
    val categoryMap: Map<Long?, List<CategoryInfoDomainModel>>,
    val channelList: List<Pair<Long?, List<ChannelInfoDomainModel>>>,
    val ownerId: Long,
    val serverId: Long,
    val serverImageUrl: String,
    val serverName: String,
)

fun ServerInfoDataModel.toDomainModel(): ServerInfoDomainModel = ServerInfoDomainModel(
    ownerId = ownerId,
    serverId = serverId,
    serverImageUrl = serverImageUrl,
    serverName = serverName,
    categoryMap = emptyMap(),
    channelList = emptyList(),
)
