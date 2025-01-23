package com.smilegate.bbebig.presentation.model

sealed interface ServerType {
    data object DM : ServerType
    data object Server : ServerType
}
