package com.smilegate.bbebig.domain.model.param

import com.smilegate.bbebig.data.request.ServerCreateRequest

data class ServerCreateParam(
    val serverName: String,
    val serverImageUrl: String?,
)

fun ServerCreateParam.toRequestModel() = ServerCreateRequest(
    serverName = serverName,
    serverImageUrl = serverImageUrl,
)
