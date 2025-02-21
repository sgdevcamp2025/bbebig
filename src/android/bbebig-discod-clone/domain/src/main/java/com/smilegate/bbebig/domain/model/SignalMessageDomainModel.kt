package com.smilegate.bbebig.domain.model

import com.smilegate.bbebig.data.model.SDP
import com.smilegate.bbebig.data.model.SignalMessageDataModel

data class SignalMessageDomainModel(
    val messageType: String,
    val channelId: String,
    val senderId: String,
    val receiverId: String?,
    val sdp: SDP?,
    val candidate: CandidateDomainModel?,
)

fun SignalMessageDataModel.toDomainModel(): SignalMessageDomainModel =
    SignalMessageDomainModel(
        messageType = messageType,
        channelId = channelId,
        senderId = senderId,
        receiverId = receiverId,
        sdp = sdp,
        candidate = candidate?.toDomainModel(),
    )

fun SignalMessageDomainModel.toDataModel(): SignalMessageDataModel =
    SignalMessageDataModel(
        messageType = messageType,
        channelId = channelId,
        senderId = senderId,
        receiverId = receiverId,
        sdp = sdp,
        candidate = candidate?.toDataModel(),
    )