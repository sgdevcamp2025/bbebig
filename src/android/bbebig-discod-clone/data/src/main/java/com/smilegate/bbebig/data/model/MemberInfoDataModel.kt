package com.smilegate.bbebig.data.model

import com.smilegate.bbebig.data.response.MemberInfoResponse

data class MemberInfoDataModel(
    val avatarUrl: String,
    val bannerUrl: String,
    val birthdate: String,
    val customPresenceStatus: String,
    val email: String,
    val userId: Long,
    val introduce: String,
    val lastAccessAt: String,
    val name: String,
    val nickname: String,
)

fun MemberInfoResponse.toDataModel(): MemberInfoDataModel = MemberInfoDataModel(
    avatarUrl = avatarUrl,
    bannerUrl = bannerUrl,
    birthdate = birthdate,
    customPresenceStatus = customPresenceStatus,
    email = email,
    userId = id,
    introduce = introduce,
    lastAccessAt = lastAccessAt,
    name = name,
    nickname = nickname,
)
