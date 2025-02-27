package com.smilegate.bbebig.domain.usecase

import com.smilegate.bbebig.data.repository.SearchRepository
import com.smilegate.bbebig.domain.model.ChatMessageDomainModel
import com.smilegate.bbebig.domain.model.toDomainModel
import dagger.Reusable
import javax.inject.Inject

@Reusable
class GetChatHistoryUseCase @Inject constructor(
    private val chatRepository: SearchRepository,
) {
    suspend operator fun invoke(serverId: Long, channelId: Long): Result<ChatMessageDomainModel> {
        return runCatching {
            chatRepository
                .getChatHistory(
                    serverId = serverId,
                    channelId = channelId,
                )
                .toDomainModel()
        }
    }
}
