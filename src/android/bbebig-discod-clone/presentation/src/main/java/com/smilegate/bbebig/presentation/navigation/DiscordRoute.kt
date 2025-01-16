package com.smilegate.bbebig.presentation.navigation

sealed interface DiscordRoute {
    val route: String

    data object Home : DiscordRoute {
        override val route: String = "home"
    }
}
