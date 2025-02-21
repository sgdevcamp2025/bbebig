package com.smilegate.bbebig.domain.usecase

import com.smilegate.bbebig.data.repository.ServerRepository
import com.smilegate.bbebig.domain.model.ServerInfoDomainModel
import com.smilegate.bbebig.domain.model.toDomainModel
import dagger.Reusable
import javax.inject.Inject

@Reusable
class GetServerInfoUseCase @Inject constructor(
    private val serverRepository: ServerRepository,
) {
    suspend operator fun invoke(serverId: Long): Result<ServerInfoDomainModel> {
        return runCatching { serverRepository.getServerInfo(serverId).toDomainModel() }
    }
}
