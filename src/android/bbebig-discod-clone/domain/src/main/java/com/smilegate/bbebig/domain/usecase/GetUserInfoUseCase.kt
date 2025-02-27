package com.smilegate.bbebig.domain.usecase

import com.smilegate.bbebig.data.repository.UserRepository
import com.smilegate.bbebig.domain.model.MemberInfoDomainModel
import com.smilegate.bbebig.domain.model.toDomainModel
import dagger.Reusable
import javax.inject.Inject

@Reusable
class GetUserInfoUseCase @Inject constructor(
    private val userRepository: UserRepository,
) {
    suspend operator fun invoke(memberId: Long): Result<MemberInfoDomainModel> =
        runCatching { userRepository.getUserInfo(memberId).toDomainModel() }
}
