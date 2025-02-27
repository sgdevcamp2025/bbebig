package com.smilegate.bbebig.data.datasource

import com.smilegate.bbebig.data.model.MemberInfoDataModel

interface UserDataSource {
    suspend fun getUserInfo(memberId: Long): MemberInfoDataModel
    suspend fun getSelfInfo(): MemberInfoDataModel
    suspend fun searchUser(nickname: String): MemberInfoDataModel
}
