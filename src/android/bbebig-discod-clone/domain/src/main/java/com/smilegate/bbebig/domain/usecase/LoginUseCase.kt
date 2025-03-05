package com.smilegate.bbebig.domain.usecase

import com.smilegate.bbebig.data.repository.AuthRepository
import com.smilegate.bbebig.domain.model.LoginDomainModel
import com.smilegate.bbebig.domain.model.param.LoginParam
import com.smilegate.bbebig.domain.model.param.toRequestModel
import com.smilegate.bbebig.domain.model.toDomainModel
import dagger.Reusable
import javax.inject.Inject

@Reusable
class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(loginParam: LoginParam): Result<LoginDomainModel> {
        return runCatching {
            authRepository.login(loginParam.toRequestModel()).toDomainModel()
        }
    }
}
