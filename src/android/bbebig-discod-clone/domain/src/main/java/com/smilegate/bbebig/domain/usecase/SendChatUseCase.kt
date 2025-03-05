package com.smilegate.bbebig.domain.usecase

import com.smilegate.bbebig.data.repository.WebSocketRepository
import com.smilegate.bbebig.domain.model.ChatDomainModel
import com.smilegate.bbebig.domain.model.toChatDataModel
import dagger.Reusable
import javax.inject.Inject

@Reusable
class SendChatUseCase @Inject constructor(
    private val webSocketRepository: WebSocketRepository,
) {
    suspend operator fun invoke(memberId: Long, chatModel: ChatDomainModel) {
        webSocketRepository.sendMessage(
            memberId = memberId,
            chatDataModel = chatModel.toChatDataModel(),
        )
    }
}
