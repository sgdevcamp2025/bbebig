package com.smilegate.bbebig.presentation.ui.channelchat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smilegate.bbebig.presentation.component.DiscordLoadingCircular
import com.smilegate.bbebig.presentation.component.DiscordTextField
import com.smilegate.bbebig.presentation.component.DiscordTopBar
import com.smilegate.bbebig.presentation.component.DiscordUserChatBox
import com.smilegate.bbebig.presentation.theme.Gray15
import com.smilegate.bbebig.presentation.theme.Gray50
import com.smilegate.bbebig.presentation.theme.Gray90
import com.smilegate.bbebig.presentation.theme.White
import com.smilegate.bbebig.presentation.ui.home.model.MessageContent
import com.smilegate.bbebig.presentation.utils.ImmutableList
import com.smilegate.bbebig.presentation.utils.LocalDateUtil
import com.smilegate.bbebig.presentation.utils.StableImage
import com.smilegate.bbebig.presentation.utils.rippleClick
import com.smilegate.devcamp.presentation.R

@Composable
fun ChannelChatScreen(
    channelName: String,
    onClickBack: () -> Unit,
    onClickSearch: () -> Unit,
    onClickAddPhoto: () -> Unit,
    onClickSendChat: (String) -> Unit,
    isLoading: Boolean,
    modifier: Modifier = Modifier,
    chatList: ImmutableList<MessageContent>,
) {
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    Box(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding(),
    ) {
        Column(
            modifier = Modifier,
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            DiscordTopBar(
                modifier = Modifier.fillMaxWidth(),
                title = channelName,
                backButtonColor = Gray90,
                onBackClick = onClickBack,
                onClickSearch = onClickSearch,
            )
            ChatContentContainer(
                modifier = Modifier
                    .heightIn(max = screenHeight - 130.dp)
                    .fillMaxSize(),
                messageList = chatList,
                isLoading = isLoading,
            )
        }
        ChatInputContainer(
            modifier = Modifier
                .background(Gray15)
                .imePadding()
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            onClickAddPhoto = onClickAddPhoto,
            onClickSendChat = onClickSendChat,
        )
    }
}

@Composable
private fun ChatContentContainer(
    modifier: Modifier,
    messageList: ImmutableList<MessageContent>,
    isLoading: Boolean,
) {
    val listState = rememberLazyListState()
    if (messageList.isNotEmpty()) {
        LaunchedEffect(messageList) {
            listState.scrollToItem(messageList.lastIndex)
        }
    }

    if (isLoading) {
        DiscordLoadingCircular(
            modifier = modifier.fillMaxSize(),
            backgroundColor = Color.Transparent,
        )
    }

    if (messageList.isEmpty()) {
        EmptyChatContainer(modifier = modifier.fillMaxSize())
    }
    LazyColumn(modifier = modifier, state = listState) {
        itemsIndexed(
            items = messageList,
            key = { _, item -> item.id },
        ) { index, content ->
            DiscordUserChatBox(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 10.dp),
                userName = content.userInfo.name,
                date = LocalDateUtil.chatTime(content.createdAt),
                chatContent = content.content,
                userIconUrl = content.userInfo.avatarUrl,
                colorNumber = content.userInfo.colorNumber,
            )
        }
    }
}

@Composable
private fun EmptyChatContainer(modifier: Modifier) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Text(
            color = White,
            fontSize = 20.sp,
            text = stringResource(R.string.empty_chat),
        )
    }
}

@Composable
private fun ChatInputContainer(
    modifier: Modifier,
    onClickAddPhoto: () -> Unit,
    onClickSendChat: (String) -> Unit,
) {
    val screenWidth = LocalConfiguration.current.screenWidthDp
    val chatTextState = rememberTextFieldState()

    Row(
        modifier = modifier
            .drawBehind {
                val strokeWidth = 1.dp.toPx()
                val y = strokeWidth / 2
                drawLine(
                    color = Color.Gray,
                    start = Offset(0f, y),
                    end = Offset(size.width, y),
                    strokeWidth = strokeWidth,
                )
            }
            .padding(vertical = 11.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        StableImage(
            modifier = Modifier
                .padding(start = 10.dp)
                .clip(shape = CircleShape)
                .background(Gray50)
                .rippleClick { onClickAddPhoto() }
                .size(30.dp)
                .padding(7.dp),
            drawableResId = R.drawable.ic_plus,
        )
        DiscordTextField(
            modifier = Modifier.width(screenWidth.dp - 100.dp),
            isSingLine = false,
            textFieldState = chatTextState,
            radius = 30.dp,
            textHint = stringResource(R.string.msg_send_hint),
            onClickSendChat = {
                if (chatTextState.text.isNotEmpty()) {
                    onClickSendChat(chatTextState.text.toString())
                    chatTextState.clearText()
                }
            },
        )
        StableImage(
            modifier = Modifier
                .clip(shape = CircleShape)
                .background(Gray50)
                .rippleClick {
                    if (chatTextState.text.isNotEmpty()) {
                        onClickSendChat(chatTextState.text.toString())
                        chatTextState.clearText()
                    }
                }
                .size(30.dp)
                .padding(7.dp),
            drawableResId = R.drawable.ic_meg_send,
        )
    }
}

@Composable
@Preview
private fun ChannelChatScreenPreview() {
    ChannelChatScreen(
        channelName = "Sample Channel",
        onClickBack = {},
        onClickSearch = {},
        onClickAddPhoto = {},
        onClickSendChat = {},
        chatList = ImmutableList(emptyList()),
        isLoading = false,
    )
}
