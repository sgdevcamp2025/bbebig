package com.smilegate.bbebig.domain.model

import com.smilegate.bbebig.data.model.MemberInfoDataModel

data class MemberInfoDomainModel(
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

fun MemberInfoDataModel.toDomainModel(): MemberInfoDomainModel {
    return MemberInfoDomainModel(
        avatarUrl = avatarUrl,
        bannerUrl = bannerUrl,
        birthdate = birthdate,
        customPresenceStatus = customPresenceStatus,
        email = email,
        userId = userId,
        introduce = introduce,
        lastAccessAt = lastAccessAt,
        name = name,
        nickname = nickname,
    )
}
