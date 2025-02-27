package com.smilegate.bbebig.domain.usecase

import com.smilegate.bbebig.domain.model.MessageContentDomainModel
import dagger.Reusable
import javax.inject.Inject

@Reusable
class GetHistoryChatListUseCase @Inject constructor(
    private val getChatHistoryUseCase: GetChatHistoryUseCase,
    private val getUserInfoUseCase: GetUserInfoUseCase,
) {
    suspend operator fun invoke(
        serverId: Long,
        channelId: Long,
    ): Result<List<MessageContentDomainModel>> {
        return runCatching {
            val historyChat = getChatHistoryUseCase(serverId, channelId).getOrThrow()

            historyChat.messages.map {
                val userInfo = getUserInfoUseCase(it.sendMemberId).getOrThrow()
                MessageContentDomainModel.toDomainModel(it, userInfo)
            }
        }
    }
}
