package com.smilegate.bbebig.presentation.ui.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.smilegate.bbebig.presentation.component.DiscordUserChatBox
import com.smilegate.bbebig.presentation.ui.search.component.SearchTextField
import com.smilegate.bbebig.presentation.utils.StableImage
import com.smilegate.bbebig.presentation.utils.noRippleClick
import com.smilegate.bbebig.presentation.utils.rippleClick
import com.smilegate.devcamp.presentation.R
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged

@Composable
fun SearchScreen(
    messageSearchResults: List<Any>,
    onClickBack: () -> Unit,
    onSearch: (String) -> Unit,
    onClickSearchResult: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        SearchContainer(
            modifier = Modifier
                .padding(vertical = 10.dp, horizontal = 20.dp)
                .fillMaxWidth(),
            onClickBack = onClickBack,
            onSearch = onSearch,
        )
        SearchResultContainer(
            modifier = Modifier.fillMaxSize(),
            messageSearchResults = messageSearchResults,
            onClickSearchResult = onClickSearchResult,
            // TODO: Add Parameter UserInfoModel
        )
    }
}

@Composable
private fun SearchResultContainer(
    modifier: Modifier,
    messageSearchResults: List<Any>,
    onClickSearchResult: () -> Unit,
) {
    if (messageSearchResults.isEmpty()) {
        EmptySearchResult(modifier = modifier)
    } else {
        LazyColumn(modifier = modifier) {
            itemsIndexed(messageSearchResults) { index, item ->
                SearchResultItem(
                    modifier = Modifier
                        .padding(vertical = 10.dp)
                        .fillMaxWidth()
                        .heightIn(max = 180.dp)
                        .wrapContentHeight(),
                    onClickSearchResult = onClickSearchResult,
                )
            }
        }
    }
}

@Composable
private fun EmptySearchResult(modifier: Modifier) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        Text(
            modifier = Modifier.padding(20.dp),
            text = "검색 결과가 없습니다!",
        )
    }
}

@Composable
private fun SearchResultItem(modifier: Modifier, onClickSearchResult: () -> Unit) {
    // TODO: Implement Real SearchResultItem
    Column(
        modifier = modifier.rippleClick { onClickSearchResult() },
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
            text = "# Channel Name",
        )
        DiscordUserChatBox(
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .fillMaxWidth(),
            userName = "User Name",
            date = "2025.01.01",
            chatContent = "Chat Content " +
                "\nChat Content\n" +
                "Chat Content\n" +
                "Chat Content\n" +
                "Chat Content\n" +
                "Chat Content\n" +
                "Chat Content\n" +
                "Chat Content\n" +
                "Chat Content\n" +
                "Chat Content\n" +
                "Chat Content\n" +
                "Chat Content\n" +
                "Chat Content",
            userIconUrl = "https://via",
        )
    }
}

@OptIn(FlowPreview::class)
@Composable
private fun SearchContainer(
    modifier: Modifier,
    onClickBack: () -> Unit,
    onSearch: (String) -> Unit,
) {
    val textState = rememberTextFieldState()

    LaunchedEffect(Unit) {
        snapshotFlow { textState.text }
            .debounce(500L)
            .distinctUntilChanged()
            .collectLatest { query ->
                if (query.isNotBlank()) {
                    onSearch(query.toString())
                }
            }
    }

    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        StableImage(
            modifier = Modifier
                .padding(end = 20.dp)
                .size(20.dp)
                .noRippleClick { onClickBack() },
            drawableResId = R.drawable.ic_back,
        )
        SearchTextField(
            modifier = Modifier.fillMaxWidth(),
            textHint = "Search",
            textFieldState = textState,
        )
    }
}

@Composable
@Preview
private fun SearchScreenPreview() {
    SearchScreen(
        onClickBack = {},
        onSearch = {},
        onClickSearchResult = {},
        messageSearchResults = listOf(
            Any(), Any(), Any(), Any(), Any(), Any(), Any(), Any(), Any(), Any(),
        ),
    )
}
