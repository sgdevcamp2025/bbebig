package com.smilegate.bbebig.data.repository

import com.smilegate.bbebig.data.datasource.UserDataSource
import com.smilegate.bbebig.data.model.MemberInfoDataModel
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userDataSource: UserDataSource,
) : UserRepository {
    override suspend fun getUserInfo(memberId: Long): MemberInfoDataModel {
        return userDataSource.getUserInfo(memberId)
    }

    override suspend fun getSelfInfo(): MemberInfoDataModel {
        return userDataSource.getSelfInfo()
    }

    override suspend fun searchUser(nickname: String): MemberInfoDataModel {
        return userDataSource.searchUser(nickname)
    }
}
