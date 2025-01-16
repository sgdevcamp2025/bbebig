package com.smilegate.bbebig.presentation.navigation

sealed interface UserRoute {
    val route: String

    data object Intro : UserRoute {
        override val route: String = "intro"
    }

    data object Login : UserRoute {
        override val route: String = "login"
    }

    data object SignUp : UserRoute {
        override val route: String = "signUp"
    }
}
