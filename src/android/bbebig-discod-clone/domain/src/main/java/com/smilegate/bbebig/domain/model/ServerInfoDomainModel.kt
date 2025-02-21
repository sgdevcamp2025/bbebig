package com.smilegate.bbebig.domain.model

import com.smilegate.bbebig.data.model.ServerInfoDataModel

data class ServerInfoDomainModel(
    val categoryInfoList: List<CategoryInfoDomainModel>,
    val channelInfoList: List<ChannelInfoDomainModel>,
    val ownerId: Long,
    val serverId: Long,
    val serverImageUrl: String,
    val serverName: String,
)

fun ServerInfoDataModel.toDomainModel(): ServerInfoDomainModel = ServerInfoDomainModel(
    categoryInfoList = categoryInfoList.map { it.toDomainModel() },
    channelInfoList = channelInfoList.map { it.toDomainModel() },
    ownerId = ownerId,
    serverId = serverId,
    serverImageUrl = serverImageUrl,
    serverName = serverName,
)
