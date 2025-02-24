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
        return runCatching {
            val serverInfo = serverRepository.getServerInfo(serverId)
            val domainModel = serverInfo.toDomainModel()
            val categoryDomainList = serverInfo.categoryInfoList.toDomainModel()
            val channelDomainList = serverInfo.channelInfoList.toDomainModel()

            if (categoryDomainList.isEmpty()) {
                domainModel.copy(
                    channelList = channelDomainList.groupBy { it.position.toLong() }.toList(),
                )
            } else {
                domainModel.copy(
                    categoryMap = categoryDomainList.groupBy { it.categoryId },
                    channelList = channelDomainList.groupBy { it.categoryId }.toList().sortedBy { it.first },
                )
            }
        }
    }
}
