package com.smilegate.bbebig.domain.usecase

import com.smilegate.bbebig.data.repository.AuthRepository
import com.smilegate.bbebig.domain.model.LoginInfoDomainModel
import com.smilegate.bbebig.domain.model.toDomainModel
import dagger.Reusable
import javax.inject.Inject

@Reusable
class LoadLoginInfoUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {
    operator fun invoke(): Result<LoginInfoDomainModel> {
        return runCatching { authRepository.loadLoginInfo().toDomainModel() }
    }
}
