package com.smilegate.bbebig.data.model

import com.smilegate.bbebig.data.response.ServerCreateResponse

data class ServerCreateDataModel(
    val serverId: Long,
)

fun ServerCreateResponse.toDataModel() = ServerCreateDataModel(
    serverId = serverId,
)
