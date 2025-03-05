package com.smilegate.bbebig.domain.model

import com.smilegate.bbebig.data.model.ChannelInfoDataModel

data class ChannelInfoDomainModel(
    val categoryId: Long,
    val channelId: Long,
    val channelMemberIdList: List<Long>,
    val channelName: String,
    val channelType: String,
    val position: Int,
    val privateStatus: Boolean,
)

fun ChannelInfoDataModel.toDomainModel(): ChannelInfoDomainModel = ChannelInfoDomainModel(
    categoryId = categoryId,
    channelId = channelId,
    channelMemberIdList = channelMemberIdList,
    channelName = channelName,
    channelType = channelType,
    position = position,
    privateStatus = privateStatus,
)

fun List<ChannelInfoDataModel>.toDomainModel(): List<ChannelInfoDomainModel> = map { it.toDomainModel() }
