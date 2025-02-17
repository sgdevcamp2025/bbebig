package com.smilegate.bbebig.domain.usecase

import com.smilegate.bbebig.data.repository.AuthRepository
import com.smilegate.bbebig.domain.model.LoginInfoDomainModel
import com.smilegate.bbebig.domain.model.toDataModel
import dagger.Reusable
import javax.inject.Inject

@Reusable
class SaveLoginInfoUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {
    operator fun invoke(loginInfo: LoginInfoDomainModel): Result<Unit> {
        return runCatching { authRepository.saveLoginInfo(loginInfo.toDataModel()) }
    }
}
