package com.smilegate.bbebig.domain.usecase

import com.smilegate.bbebig.data.repository.WebSocketRepository
import com.smilegate.bbebig.domain.model.SignalMessageDomainModel
import com.smilegate.bbebig.domain.model.toDomainModel
import dagger.Reusable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@Reusable
class ReceiveChannelGroupMessageUseCase @Inject constructor(
    private val webSocketRepository: WebSocketRepository,
) {
    operator fun invoke(channelId: String): Flow<SignalMessageDomainModel> {
        return webSocketRepository
            .receiveChannelGroupMessage(channelId = channelId)
            .map {
                it.toDomainModel()
            }
    }
}