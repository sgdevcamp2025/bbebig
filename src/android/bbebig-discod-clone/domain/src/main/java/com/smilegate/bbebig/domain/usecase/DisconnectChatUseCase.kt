package com.smilegate.bbebig.domain.usecase

import com.smilegate.bbebig.data.repository.WebSocketRepository
import dagger.Reusable
import javax.inject.Inject

@Reusable
class DisconnectChatUseCase @Inject constructor(
    private val webSocketRepository: WebSocketRepository,
) {
    suspend operator fun invoke(): Result<Unit> {
        return runCatching { webSocketRepository.disconnect() }
    }
}
