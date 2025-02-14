package com.smilegate.bbebig.presentation.ui.search.navigation

import androidx.compose.runtime.Composable
import com.smilegate.bbebig.presentation.ui.search.SearchScreen

@Composable
fun SearchRoute(
    onBackClick: () -> Unit,
) {
    // TODO: Implement SearchRoute
    SearchScreen(
        onClickBack = onBackClick,
        onSearch = {},
        onClickSearchResult = {},
        messageSearchResults = emptyList(),
    )
}
