package com.smilegate.bbebig.domain.usecase

import com.smilegate.bbebig.data.repository.WebSocketRepository
import com.smilegate.bbebig.domain.model.SignalMessageDomainModel
import com.smilegate.bbebig.domain.model.toDomainModel
import dagger.Reusable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@Reusable
class ReceiveUserGroupMessageUseCase @Inject constructor(
    private val webSocketRepository: WebSocketRepository,
) {
    operator fun invoke(userId: String): Flow<SignalMessageDomainModel> {
        return webSocketRepository
            .receiveUserGroupMessage(userId = userId)
            .map {
                it.toDomainModel()
            }
    }
}