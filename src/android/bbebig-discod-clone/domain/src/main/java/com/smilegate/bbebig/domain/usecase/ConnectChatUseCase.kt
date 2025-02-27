package com.smilegate.bbebig.domain.usecase

import com.smilegate.bbebig.data.repository.WebSocketRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ConnectChatUseCase @Inject constructor(
    private val webSocketRepository: WebSocketRepository,
) {
    suspend operator fun invoke(
        memberId: Long,
        roomType: String,
        channelId: Long,
        serverId: Long,
    ): Result<Unit> {
        return runCatching {
            webSocketRepository.connect(
                memberId,
                roomType,
                channelId,
                serverId,
            )
        }
    }
}
