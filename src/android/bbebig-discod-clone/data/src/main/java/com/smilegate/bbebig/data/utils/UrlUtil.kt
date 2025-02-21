package com.smilegate.bbebig.data.utils

object UrlUtil {
    fun parseChannelUrl(baseUrl: String, channelId: String): String {
        return "$baseUrl/$channelId"
    }

    fun parseUserUrl( userId: String): String {
        return "/sub/stream/direct/$userId"
    }
}