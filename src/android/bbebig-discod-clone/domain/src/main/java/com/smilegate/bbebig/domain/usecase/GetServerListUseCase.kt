package com.smilegate.bbebig.domain.usecase

import com.smilegate.bbebig.data.repository.ServerRepository
import com.smilegate.bbebig.domain.model.ServerDomainModel
import com.smilegate.bbebig.domain.model.toDomainModel
import dagger.Reusable
import javax.inject.Inject

@Reusable
class GetServerListUseCase @Inject constructor(
    private val serverRepository: ServerRepository,
) {
    suspend operator fun invoke(): List<ServerDomainModel> =
        serverRepository.getServerList().toDomainModel()
}
