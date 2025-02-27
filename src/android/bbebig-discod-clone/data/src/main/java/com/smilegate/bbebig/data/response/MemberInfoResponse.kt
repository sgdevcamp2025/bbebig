package com.smilegate.bbebig.data.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MemberInfoResponse(
    @SerialName("avatarUrl")
    val avatarUrl: String = "",
    @SerialName("bannerUrl")
    val bannerUrl: String = "",
    @SerialName("birthdate")
    val birthdate: String = "",
    @SerialName("customPresenceStatus")
    val customPresenceStatus: String = "",
    @SerialName("email")
    val email: String = "",
    @SerialName("id")
    val id: Long = 0,
    @SerialName("introduce")
    val introduce: String = "",
    @SerialName("lastAccessAt")
    val lastAccessAt: String = "",
    @SerialName("name")
    val name: String = "",
    @SerialName("nickname")
    val nickname: String = "",
)
