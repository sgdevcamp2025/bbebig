package com.smilegate.bbebig.domain.usecase

import com.smilegate.bbebig.data.repository.UserRepository
import com.smilegate.bbebig.domain.model.MemberInfoDomainModel
import com.smilegate.bbebig.domain.model.toDomainModel
import dagger.Reusable
import javax.inject.Inject

@Reusable
class SearchUserInfoUseCase @Inject constructor(
    private val userRepository: UserRepository,
) {
    suspend fun invoke(nickname: String): Result<MemberInfoDomainModel> =
        runCatching { userRepository.searchUser(nickname).toDomainModel() }
}
