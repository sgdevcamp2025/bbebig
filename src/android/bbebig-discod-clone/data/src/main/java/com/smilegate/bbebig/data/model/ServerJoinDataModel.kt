package com.smilegate.bbebig.data.model

import com.smilegate.bbebig.data.response.ServerJoinResponse

data class ServerJoinDataModel(
    val serverId: Long,
)

fun ServerJoinResponse.toDataModel() = ServerJoinDataModel(
    serverId = serverId,
)
