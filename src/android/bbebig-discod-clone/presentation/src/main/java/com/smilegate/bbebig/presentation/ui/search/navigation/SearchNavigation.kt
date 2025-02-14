package com.smilegate.bbebig.presentation.ui.search.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.smilegate.bbebig.presentation.navigation.Search

fun NavController.navigateToSearch() {
    navigate(
        route = Search,
    )
}

fun NavGraphBuilder.searchNavigation(
    onBackClick: () -> Unit,
) {
    composable<Search> {
        SearchRoute(
            onBackClick = onBackClick,
        )
    }
}
