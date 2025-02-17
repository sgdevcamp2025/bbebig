package com.smilegate.bbebig.domain.usecase

import com.smilegate.bbebig.data.repository.AuthRepository
import dagger.Reusable
import javax.inject.Inject

@Reusable
class GetRefreshTokenUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {
    operator fun invoke(): Result<String> {
        return runCatching {
            authRepository.getRefreshToken() ?: throw NullPointerException()
        }
    }
}
