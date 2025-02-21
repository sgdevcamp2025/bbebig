package com.smilegate.bbebig.domain.model

import com.smilegate.bbebig.data.model.ServerJoinDataModel

data class ServerJoinDomainModel(
    val serverId: Long,
)

fun ServerJoinDataModel.toDomainModel() = ServerJoinDomainModel(
    serverId = serverId,
)