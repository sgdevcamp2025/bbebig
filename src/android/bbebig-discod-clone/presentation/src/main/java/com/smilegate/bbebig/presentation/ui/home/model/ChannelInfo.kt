package com.smilegate.bbebig.presentation.ui.home.model

import com.smilegate.bbebig.domain.model.ChannelInfoDomainModel

data class ChannelInfo(
    val categoryId: Long,
    val channelId: Long,
    val channelMemberIdList: List<Long>,
    val channelName: String,
    val channelType: String,
    val position: Int,
    val privateStatus: Boolean,
)

fun ChannelInfoDomainModel.toUiModel(): ChannelInfo = ChannelInfo(
    categoryId = categoryId,
    channelId = channelId,
    channelMemberIdList = channelMemberIdList,
    channelName = channelName,
    channelType = channelType,
    position = position,
    privateStatus = privateStatus,
)

fun List<ChannelInfoDomainModel>.toUiModel(): List<ChannelInfo> = map { it.toUiModel() }
