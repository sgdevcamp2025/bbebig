package com.smilegate.bbebig.presentation.model

data class SampleServerList(
    val serverId: Long,
    val serverName: String = "serverName",
    val channelList: List<SampleChannel>,
) {
    companion object {
        fun getDummyList(): SampleServerList {
            return SampleServerList(
                serverId = 1,
                serverName = "serverName2323",
                channelList = listOf(
                    SampleChannel(
                        channelId = 1,
                        channelName = "channelName1",
                        subChannelList = listOf(
                            SampleChannelInfo(
                                subChannelId = 1,
                                subChannelName = "subChannelName1",
                            ),
                            SampleChannelInfo(
                                subChannelId = 2,
                                subChannelName = "subChannelName2",
                            ),
                            SampleChannelInfo(
                                subChannelId = 3,
                                subChannelName = "subChannelName3",
                            ),
                        ),
                    ),
                    SampleChannel(
                        channelId = 2,
                        channelName = "channelName2",
                        subChannelList = listOf(
                            SampleChannelInfo(
                                subChannelId = 4,
                                subChannelName = "subChannelName4",
                            ),
                            SampleChannelInfo(
                                subChannelId = 5,
                                subChannelName = "subChannelName5",
                            ),
                            SampleChannelInfo(
                                subChannelId = 6,
                                subChannelName = "subChannelName6",
                            ),
                        ),
                    ),
                    SampleChannel(
                        channelId = 3,
                        channelName = "channelName3",
                        subChannelList = listOf(
                            SampleChannelInfo(
                                subChannelId = 7,
                                subChannelName = "subChannelName7",
                            ),
                            SampleChannelInfo(
                                subChannelId = 8,
                                subChannelName = "subChannelName8",
                            ),
                            SampleChannelInfo(
                                subChannelId = 9,
                                subChannelName = "subChannelName9",
                            ),
                        ),
                    ),
                ),
            )
        }
    }
}

data class SampleChannel(
    val channelId: Long,
    val channelName: String,
    val subChannelList: List<SampleChannelInfo>,
)

data class SampleChannelInfo(
    val subChannelId: Long,
    val subChannelName: String,
)
