package com.smilegate.bbebig.data.model

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.webrtc.SessionDescription

@Serializable
data class SignalMessageDataModel(
    @SerialName("messageType")
    val messageType: String = "",
    @SerialName("channelId")
    val channelId: String = "",
    @SerialName("senderId")
    val senderId: String = "",
    @SerialName("receiverId")
    val receiverId: String?,
    @SerialName("sdp")
    val sdp: SDP?,
    @SerialName("candidate")
    val candidate: CandidateDataModel?,
    @SerialName("participants")
    val participants: List<Long>? = null,
)
