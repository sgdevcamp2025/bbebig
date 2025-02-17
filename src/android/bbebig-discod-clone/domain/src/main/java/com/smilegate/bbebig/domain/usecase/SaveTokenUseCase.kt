package com.smilegate.bbebig.domain.usecase

import com.smilegate.bbebig.data.repository.AuthRepository
import dagger.Reusable
import javax.inject.Inject

@Reusable
class SaveTokenUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {
    operator fun invoke(accessToken: String, refreshToken: String): Result<Unit> {
        return runCatching { authRepository.saveToken(accessToken, refreshToken) }
    }
}
