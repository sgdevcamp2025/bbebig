package com.smilegate.bbebig.presentation.ui.home.model

enum class ChatRoomType {
    VOICE,
    CHAT,
    ;

    companion object {
        fun matchType(type: String): ChatRoomType {
            return try {
                enumValueOf(type)
            } catch (e: IllegalArgumentException) {
                throw IllegalArgumentException("Unknown type: $type", e)
            }
        }
    }
}
