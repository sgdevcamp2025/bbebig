package com.smilegate.bbebig.presentation.ui.home.model

import com.smilegate.bbebig.domain.model.ServerInfoDomainModel
import com.smilegate.bbebig.presentation.utils.ImmutableList

data class ServerInfo(
    val categoryMap: Map<Long?, List<CategoryInfo>>,
    val channelList: ImmutableList<Pair<Long?, List<ChannelInfo>>>,
    val ownerId: Long,
    val serverId: Long,
    val serverImageUrl: String,
    val serverName: String,
)

fun ServerInfoDomainModel.toUiModel(): ServerInfo = ServerInfo(
    categoryMap = categoryMap.mapValues { it.value.map { it.toUiModel() } },
    channelList = ImmutableList(channelList.map { it.first to it.second.map { it.toUiModel() } }),
    ownerId = ownerId,
    serverId = serverId,
    serverImageUrl = serverImageUrl,
    serverName = serverName,
)
