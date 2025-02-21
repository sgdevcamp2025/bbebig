package com.smilegate.bbebig.data.model

import com.smilegate.bbebig.data.response.ChannelInfoResponse

data class ChannelInfoDataModel(
    val categoryId: Long,
    val channelId: Long,
    val channelMemberIdList: List<Long>,
    val channelName: String,
    val channelType: String,
    val position: Int,
    val privateStatus: Boolean,
)

fun ChannelInfoResponse.toDataModel(): ChannelInfoDataModel = ChannelInfoDataModel(
    categoryId = categoryId,
    channelId = channelId,
    channelMemberIdList = channelMemberIdList,
    channelName = channelName,
    channelType = channelType,
    position = position,
    privateStatus = privateStatus,
)

fun List<ChannelInfoResponse>.toDataModel(): List<ChannelInfoDataModel> = map { it.toDataModel() }
