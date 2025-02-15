package com.smilegate.bbebig.domain.usecase

import com.smilegate.bbebig.data.repository.AuthRepository
import com.smilegate.bbebig.domain.model.SignUpDomainModel
import com.smilegate.bbebig.domain.model.param.SignUpParam
import com.smilegate.bbebig.domain.model.param.toRequestModel
import com.smilegate.bbebig.domain.model.toDomainModel
import dagger.Reusable
import javax.inject.Inject

@Reusable
class SignUpUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(signUpParam: SignUpParam): Result<SignUpDomainModel> =
        runCatching { authRepository.signUp(signUpParam.toRequestModel()).toDomainModel() }
}
