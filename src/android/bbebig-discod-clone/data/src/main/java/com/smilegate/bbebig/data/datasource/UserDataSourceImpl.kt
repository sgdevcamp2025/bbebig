package com.smilegate.bbebig.data.datasource

import com.smilegate.bbebig.data.api.UserApiService
import com.smilegate.bbebig.data.base.callApi
import com.smilegate.bbebig.data.model.MemberInfoDataModel
import com.smilegate.bbebig.data.model.toDataModel
import javax.inject.Inject

class UserDataSourceImpl @Inject constructor(
    private val userApiService: UserApiService,
) : UserDataSource {
    override suspend fun getUserInfo(memberId: Long): MemberInfoDataModel {
        return callApi { userApiService.getUserInfo(memberId) }.toDataModel()
    }

    override suspend fun getSelfInfo(): MemberInfoDataModel {
        return callApi { userApiService.getSelfInfo() }.toDataModel()
    }

    override suspend fun searchUser(nickname: String): MemberInfoDataModel {
        return callApi { userApiService.searchUser(nickname) }.toDataModel()
    }
}
