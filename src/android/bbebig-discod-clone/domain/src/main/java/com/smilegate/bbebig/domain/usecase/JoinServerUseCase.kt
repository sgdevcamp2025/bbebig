package com.smilegate.bbebig.domain.usecase

import com.smilegate.bbebig.data.repository.ServerRepository
import com.smilegate.bbebig.domain.model.ServerJoinDomainModel
import com.smilegate.bbebig.domain.model.toDomainModel
import dagger.Reusable
import javax.inject.Inject

@Reusable
class JoinServerUseCase @Inject constructor(
    private val serverRepository: ServerRepository
) {
    suspend operator fun invoke(serverId: Long): ServerJoinDomainModel {
        return serverRepository.joinServer(serverId).toDomainModel()
    }
}