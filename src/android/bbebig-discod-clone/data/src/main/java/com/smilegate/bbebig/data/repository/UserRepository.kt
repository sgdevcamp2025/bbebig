package com.smilegate.bbebig.data.repository

import com.smilegate.bbebig.data.model.MemberInfoDataModel

interface UserRepository {
    suspend fun getUserInfo(memberId: Long): MemberInfoDataModel
    suspend fun getSelfInfo(): MemberInfoDataModel
    suspend fun searchUser(nickname: String): MemberInfoDataModel
}
