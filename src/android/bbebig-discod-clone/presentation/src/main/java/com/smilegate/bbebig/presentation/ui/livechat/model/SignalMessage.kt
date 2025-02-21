package com.smilegate.bbebig.presentation.ui.livechat.model

import com.smilegate.bbebig.data.model.SDP
import com.smilegate.bbebig.domain.model.SignalMessageDomainModel

data class SignalMessage(
    val messageType: String,
    val channelId: String,
    val senderId: String,
    val receiverId: String? = null,
    val sdp: SDP? = null,
    val candidate: Candidate? = null,
)

fun SignalMessageDomainModel.toUiModel() = SignalMessage(
    messageType = messageType,
    channelId = channelId,
    senderId = senderId,
    receiverId = receiverId,
    sdp = sdp,
    candidate = candidate?.toUiModel(),
)

fun SignalMessage.toDomainModel() = SignalMessageDomainModel(
    messageType = messageType,
    channelId = channelId,
    senderId = senderId,
    receiverId = receiverId,
    sdp = sdp,
    candidate = candidate?.toDomainModel(),
)