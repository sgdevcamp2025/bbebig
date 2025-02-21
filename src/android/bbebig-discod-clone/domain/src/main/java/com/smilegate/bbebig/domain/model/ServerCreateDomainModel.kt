package com.smilegate.bbebig.domain.model

import com.smilegate.bbebig.data.model.ServerCreateDataModel

data class ServerCreateDomainModel(
    val serverId: Long,
)

fun ServerCreateDataModel.toDomainModel() = ServerCreateDomainModel(
    serverId = serverId,
)
