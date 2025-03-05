package com.smilegate.bbebig.domain.usecase

import com.smilegate.bbebig.data.repository.WebSocketRepository
import com.smilegate.bbebig.domain.model.ChatDomainModel
import com.smilegate.bbebig.domain.model.toChatDomainModel
import dagger.Reusable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@Reusable
class ReceiveChatUseCase @Inject constructor(
    private val webSocketRepository: WebSocketRepository,
) {
    suspend operator fun invoke(serverId: Long, memberId: Long): Flow<ChatDomainModel> {
        return webSocketRepository
            .receiveMessage(serverId, memberId)
            .map { it.toChatDomainModel() }
    }
}
