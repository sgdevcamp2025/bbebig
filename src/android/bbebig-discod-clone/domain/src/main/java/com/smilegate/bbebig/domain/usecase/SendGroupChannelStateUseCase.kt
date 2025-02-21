package com.smilegate.bbebig.domain.usecase

import com.smilegate.bbebig.data.repository.WebSocketRepository
import com.smilegate.bbebig.domain.model.SignalMessageDomainModel
import com.smilegate.bbebig.domain.model.toDataModel
import dagger.Reusable
import javax.inject.Inject

@Reusable
class SendGroupChannelStateUseCase @Inject constructor(
    private val webSocketRepository: WebSocketRepository,
) {
    suspend operator fun invoke(channelState: SignalMessageDomainModel) {
        webSocketRepository.sendChannelStateGroupMessage(channelState.toDataModel())
    }
}