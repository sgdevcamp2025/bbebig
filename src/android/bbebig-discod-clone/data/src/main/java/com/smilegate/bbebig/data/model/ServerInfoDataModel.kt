package com.smilegate.bbebig.data.model

import com.smilegate.bbebig.data.response.ServerInfoResponse

data class ServerInfoDataModel(
    val categoryInfoList: List<CategoryInfoDataModel>,
    val channelInfoList: List<ChannelInfoDataModel>,
    val ownerId: Long,
    val serverId: Long,
    val serverImageUrl: String,
    val serverName: String,
)

fun ServerInfoResponse.toDataModel(): ServerInfoDataModel = ServerInfoDataModel(
    categoryInfoList = categoryInfoList.toDataModel(),
    channelInfoList = channelInfoList.toDataModel(),
    ownerId = ownerId,
    serverId = serverId,
    serverImageUrl = serverImageUrl,
    serverName = serverName,
)
