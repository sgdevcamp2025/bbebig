package com.smilegate.bbebig.presentation.ui.home.model

import com.smilegate.bbebig.domain.model.MemberInfoDomainModel
import com.smilegate.bbebig.presentation.utils.RandomUtil

data class MemberInfo(
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
    val colorNumber: Int?,
)

fun MemberInfoDomainModel.toUiModel(): MemberInfo {
    return MemberInfo(
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
        colorNumber = if (avatarUrl.isEmpty()) {
            RandomUtil.generate(userId)
        } else {
            null
        },
    )
}
