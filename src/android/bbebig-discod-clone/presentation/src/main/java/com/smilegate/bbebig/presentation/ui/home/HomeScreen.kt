package com.smilegate.bbebig.presentation.ui.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.ModalBottomSheetLayout
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.ModalBottomSheetValue
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.smilegate.bbebig.presentation.component.DiscordParticipantContainer
import com.smilegate.bbebig.presentation.component.DiscordRoundButton
import com.smilegate.bbebig.presentation.component.DiscordTitleContainer
import com.smilegate.bbebig.presentation.theme.Blue70
import com.smilegate.bbebig.presentation.theme.Gray10
import com.smilegate.bbebig.presentation.theme.Gray15
import com.smilegate.bbebig.presentation.theme.Gray30
import com.smilegate.bbebig.presentation.theme.Gray40
import com.smilegate.bbebig.presentation.theme.Gray50
import com.smilegate.bbebig.presentation.theme.Gray60
import com.smilegate.bbebig.presentation.theme.Gray70
import com.smilegate.bbebig.presentation.theme.Gray80
import com.smilegate.bbebig.presentation.theme.White
import com.smilegate.bbebig.presentation.ui.channelchat.ChannelChatScreen
import com.smilegate.bbebig.presentation.ui.home.model.CategoryInfo
import com.smilegate.bbebig.presentation.ui.home.model.ChannelInfo
import com.smilegate.bbebig.presentation.ui.home.model.Server
import com.smilegate.bbebig.presentation.ui.home.model.ServerInfo
import com.smilegate.bbebig.presentation.ui.home.mvi.HomeUiState
import com.smilegate.bbebig.presentation.utils.ImmutableList
import com.smilegate.bbebig.presentation.utils.StableImage
import com.smilegate.bbebig.presentation.utils.noRippleSingleClick
import com.smilegate.bbebig.presentation.utils.rippleClick
import com.smilegate.bbebig.presentation.utils.rippleSingleClick
import com.smilegate.devcamp.presentation.R

@Composable
fun HomeScreen(
    uiState: HomeUiState,
    onMakeServerClick: () -> Unit,
    onServerJoinClick: () -> Unit,
    onClickInviteFriend: () -> Unit,
    onClickJoinLiveChat: () -> Unit,
    onSearchClick: () -> Unit,
    onSubChannelClick: (ChannelInfo) -> Unit,
    onClickBackChatRoom: () -> Unit,
    onServerClick: (Long) -> Unit,
    onDisMissSheet: () -> Unit,
    onClickSendChat: (String) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(Gray10)
            .statusBarsPadding(),
    ) {
        ServerListContainer(
            modifier = Modifier.fillMaxHeight(),
            serverList = uiState.serverList,
            onMakeServerClick = onMakeServerClick,
            onServerClick = onServerClick,
        )
        ServerChannelContainer(
            modifier = Modifier.background(color = Gray30),
            serverInfo = uiState.serverInfo,
            onMakeServerClick = onMakeServerClick,
            onServerJoinClick = onServerJoinClick,
            onSearchClick = onSearchClick,
            onClickAddFriend = onClickInviteFriend,
            onSubChannelClick = onSubChannelClick,
        )
    }
    LiveChatJoinBottomSheet(
        modifier = Modifier.fillMaxWidth(),
        isVisible = uiState.isSheetVisible,
        channelName = uiState.selectedChannelInfo.channelName,
        participantList = emptyList(),
        onClickMuteMic = {},
        onClickJoinLiveChat = onClickJoinLiveChat,
        onClickChannelChat = {},
        onClickInviteUser = {},
        onDisMissSheet = onDisMissSheet,
    )
    if (uiState.isChatRoomVisible) {
        ChannelChatScreen(
            modifier = Modifier
                .fillMaxSize()
                .background(Gray15),
            channelName = uiState.selectedChannelInfo.channelName,
            chatList = uiState.receiveChatMessageList,
            onClickBack = onClickBackChatRoom,
            isLoading = uiState.isLoading,
            onClickSearch = {},
            onClickAddPhoto = {},
            onClickSendChat = onClickSendChat,
        )
    }
}

@Composable
private fun ServerListContainer(
    modifier: Modifier,
    serverList: ImmutableList<Server>,
    onMakeServerClick: () -> Unit,
    onServerClick: (Long) -> Unit,
) {
    val selectedServerId = remember { mutableIntStateOf(Int.MIN_VALUE) }

    LazyColumn(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        itemsIndexed(
            items = serverList,
            key = { _, server ->
                server.serverId
            },
        ) { index, server ->
            if (index == 0) {
                Spacer(modifier = Modifier.height(10.dp))
            }

            ServerItem(
                modifier = Modifier,
                isSelected = selectedServerId.intValue == index,
                index = index,
                imageUrl = server.serverImageUrl,
                onServerClick = { clickedIndex ->
                    selectedServerId.intValue = clickedIndex
                    onServerClick(server.serverId)
                },
            )
        }
        item {
            MakeServerItem(modifier = Modifier, onMakeServerClick = onMakeServerClick)
        }
    }
}

@Composable
private fun TopTitleContainer(modifier: Modifier, title: String, onClickSearch: () -> Unit) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            modifier = Modifier.padding(end = 5.dp),
            text = title,
            fontSize = 20.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        StableImage(
            modifier = Modifier
                .size(14.dp)
                .rotate(-90f)
                .noRippleSingleClick { onClickSearch() },
            drawableResId = R.drawable.ic_dropdown_menu,
        )
    }
}

@Composable
private fun EmptyServerContent(
    modifier: Modifier,
    onMakeServerClick: () -> Unit,
    onServerJoinClick: () -> Unit,
) {
    Box(modifier = modifier) {
        Text(modifier = Modifier.align(Alignment.TopStart), fontSize = 24.sp, text = "서버")
        DiscordTitleContainer(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(vertical = 20.dp),
            firstTitleResId = R.string.empty_title,
            secondTitleResId = R.string.empty_sub_title,
        )
        ButtonContainer(
            modifier = Modifier.align(Alignment.BottomCenter),
            onMakeServerClick = onMakeServerClick,
            onServerJoinClick = onServerJoinClick,
        )
    }
}

@Composable
private fun ButtonContainer(
    modifier: Modifier,
    onMakeServerClick: () -> Unit,
    onServerJoinClick: () -> Unit,
) {
    Column(modifier = modifier, Arrangement.spacedBy(15.dp)) {
        DiscordRoundButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = onServerJoinClick,
            textResId = R.string.server_join_text,
            textColor = Color.White,
            backgroundColor = Blue70,
        )
        DiscordRoundButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = onMakeServerClick,
            textResId = R.string.server_make_text,
            textColor = Gray80,
            backgroundColor = Gray50,
        )
    }
}

@Composable
private fun ServerItem(
    modifier: Modifier,
    isSelected: Boolean,
    index: Int,
    onServerClick: (Int) -> Unit,
    imageUrl: String? = null,
    drawableResId: Int? = null,
) {
    val cornerRadius by animateDpAsState(
        targetValue = if (isSelected) 15.dp else 50.dp,
        animationSpec = tween(durationMillis = 400, easing = LinearOutSlowInEasing),
        label = "",
    )

    Box(modifier = modifier) {
        AsyncImage(
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .graphicsLayer {
                    clip = true
                    shape = RoundedCornerShape(cornerRadius)
                }
                .size(50.dp)
                .background(color = if (isSelected) Blue70 else Gray60)
                .clickable { onServerClick(index) },
            model = drawableResId ?: imageUrl,
            contentDescription = null,
            contentScale = ContentScale.Crop,
        )
        ServerItemIndicator(
            modifier = Modifier.align(Alignment.CenterStart),
            isSelect = isSelected,
        )
    }
}

@Composable
private fun ServerItemIndicator(modifier: Modifier, isSelect: Boolean) {
    AnimatedVisibility(
        modifier = modifier,
        enter = fadeIn(
            animationSpec = tween(
                durationMillis = 400,
                easing = FastOutSlowInEasing,
            ),
        ) + expandIn(),
        exit = shrinkOut() + fadeOut(
            animationSpec = tween(
                durationMillis = 400,
                easing = FastOutSlowInEasing,
            ),
        ),
        visible = isSelect,
    ) {
        Column(
            modifier = Modifier
                .width(4.dp)
                .height(40.dp)
                .background(
                    color = Color.Blue,
                    shape = RoundedCornerShape(topEnd = 100.dp, bottomEnd = 100.dp),
                ),
            content = { Unit },
        )
    }
}

@Composable
private fun MakeServerItem(modifier: Modifier, onMakeServerClick: () -> Unit) {
    StableImage(
        modifier = modifier
            .padding(start = 10.dp, end = 10.dp, bottom = 10.dp)
            .clip(shape = CircleShape)
            .background(Gray70)
            .rippleSingleClick { onMakeServerClick() }
            .padding(13.dp),
        colorFilter = ColorFilter.tint(color = White),
        drawableResId = R.drawable.ic_plus,
    )
}

@Composable
private fun ServerChannelContainer(
    modifier: Modifier,
    serverInfo: ServerInfo,
    onMakeServerClick: () -> Unit,
    onServerJoinClick: () -> Unit,
    onSearchClick: () -> Unit,
    onClickAddFriend: () -> Unit,
    onSubChannelClick: (ChannelInfo) -> Unit,
) {
    if (serverInfo.channelList.isEmpty()) {
        EmptyServerContent(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = 15.dp, vertical = 20.dp),
            onMakeServerClick = onMakeServerClick,
            onServerJoinClick = onServerJoinClick,
        )
    } else {
        ServerContentContainer(
            modifier = modifier
                .fillMaxSize()
                .padding(top = 5.dp),
            onSearchClick = onSearchClick,
            serverName = serverInfo.serverName,
            channelInfoList = serverInfo.channelList,
            categoryMap = serverInfo.categoryMap,
            onSubChannelClick = onSubChannelClick,
        )
    }
}

@Composable
private fun DMTopContainer(
    modifier: Modifier,
    onClickAddFriend: () -> Unit,
    onClickSearch: () -> Unit,
) {
    Column(modifier = modifier) {
        Text(
            modifier = Modifier.padding(vertical = 10.dp),
            text = stringResource(R.string.dm_top_title),
            fontSize = 24.sp,
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            StableImage(
                modifier = Modifier
                    .size(30.dp)
                    .clip(shape = CircleShape)
                    .background(color = Gray40)
                    .padding(8.dp)
                    .clickable { onClickSearch() },
                drawableResId = R.drawable.ic_search,
            )
            DiscordRoundButton(
                modifier = Modifier.fillMaxWidth(),
                textResId = R.string.add_friend,
                verticalInnerPadding = 8.dp,
                textColor = Gray80,
                iconSize = 10.dp,
                textSize = 12.sp,
                backgroundColor = Gray60,
                onClick = onClickAddFriend,
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun DMContentContainer(
    modifier: Modifier,
    onClickAddFriend: () -> Unit,
    onClickSearch: () -> Unit,
) {
    val friendList = remember { mutableStateOf(emptyList<String>()) }

    Box(modifier = modifier.fillMaxSize()) {
        LazyColumn(modifier = Modifier.align(Alignment.TopCenter)) {
            stickyHeader {
                DMTopContainer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 15.dp),
                    onClickAddFriend = onClickAddFriend,
                    onClickSearch = onClickSearch,
                )
            }
        }

        if (friendList.value.isEmpty()) {
            EmptyFriendListContainer(
                modifier = Modifier.align(Alignment.Center),
                onClickAddFriend = onClickAddFriend,
            )
        }
    }
}

@Composable
private fun EmptyFriendListContainer(modifier: Modifier, onClickAddFriend: () -> Unit) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        DiscordTitleContainer(
            modifier = Modifier.wrapContentSize(),
            firstTitleResId = R.string.dm_empty_title,
            secondTitleResId = R.string.dm_sub_title,
        )
        DiscordRoundButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 20.dp),
            textResId = R.string.add_friend,
            textColor = White,
            backgroundColor = Blue70,
            onClick = onClickAddFriend,
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ServerContentContainer(
    modifier: Modifier,
    channelInfoList: ImmutableList<Pair<Long?, List<ChannelInfo>>>,
    categoryMap: Map<Long?, List<CategoryInfo>>,
    serverName: String,
    onSearchClick: () -> Unit,
    onSubChannelClick: (ChannelInfo) -> Unit,
) {
    LazyColumn(modifier = modifier) {
        stickyHeader {
            Column(modifier = modifier.padding(horizontal = 15.dp)) {
                TopTitleContainer(
                    modifier = Modifier,
                    title = serverName,
                    onClickSearch = onSearchClick,
                )
                DiscordRoundButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),
                    textResId = R.string.search_text,
                    drawableRedId = R.drawable.ic_search,
                    onClick = onSearchClick,
                    textColor = Gray80,
                    backgroundColor = Gray60,
                )
            }
            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
                thickness = 1.dp,
                color = Gray80,
            )
        }

        itemsIndexed(channelInfoList) { index, categoryInfo ->
            ChannelItem(
                modifier = Modifier.padding(vertical = 5.dp),
                categoryName = categoryMap.getOrDefault(categoryInfo.first, emptyList())
                    .getOrNull(0)?.categoryName ?: "",
                channelInfo = categoryInfo.second,
                onChanelClick = { },
                onSubChannelClick = onSubChannelClick,
            )
        }
    }
}

@Composable
private fun ChannelItem(
    modifier: Modifier,
    categoryName: String,
    channelInfo: List<ChannelInfo>,
    onChanelClick: () -> Unit,
    onSubChannelClick: (ChannelInfo) -> Unit,
) {
    var isChannelSelected by rememberSaveable { mutableStateOf(false) }

    Column(modifier = modifier) {
        if (categoryName.isNotEmpty()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isChannelSelected = !isChannelSelected },
                verticalAlignment = Alignment.CenterVertically,
            ) {
                StableImage(
                    modifier = Modifier
                        .size(10.dp)
                        .rotate(if (isChannelSelected) -90f else 0f),
                    drawableResId = R.drawable.ic_dropdown_menu,
                )
                Text(text = categoryName)
            }
        }

        if (isChannelSelected) {
            Column(
                modifier = Modifier.padding(horizontal = 15.dp, vertical = 5.dp),
            ) {
                channelInfo.forEach { subChannel ->
                    Text(
                        modifier = Modifier
                            .clip(RoundedCornerShape(5.dp))
                            .fillMaxWidth()
                            .rippleClick { onSubChannelClick(subChannel) },
                        text = subChannel.channelName,
                    )
                }
            }
        }
    }
}

@Composable
private fun LiveChatJoinBottomSheet(
    modifier: Modifier,
    isVisible: Boolean,
    channelName: String,
    participantList: List<Any>,
    onClickMuteMic: () -> Unit,
    onClickJoinLiveChat: () -> Unit,
    onClickChannelChat: () -> Unit,
    onClickInviteUser: () -> Unit,
    onDisMissSheet: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = true,
        confirmValueChange = { newValue ->
            if (newValue == ModalBottomSheetValue.Hidden) {
                onDisMissSheet()
            }
            true
        },
    )

    ModalBottomSheetLayout(
        modifier = modifier,
        sheetState = sheetState,
        sheetShape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        sheetContent = {
            BottomSheetTopContainer(
                modifier = Modifier
                    .background(color = Gray10)
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 10.dp),
                channelName = channelName,
                onClickInviteUser = onClickInviteUser,
            )
            Box {
                BottomSheetParticipateContainer(
                    modifier = Modifier
                        .heightIn(max = 500.dp)
                        .fillMaxSize()
                        .background(Gray10),
                    participantList = participantList,
                )
                BottomUtilBarContainer(
                    modifier = Modifier
                        .padding(start = 10.dp, end = 10.dp, bottom = 5.dp)
                        .fillMaxWidth()
                        .clip(shape = RoundedCornerShape(30.dp))
                        .background(Gray40)
                        .align(Alignment.BottomCenter),
                    onClickMuteMic = onClickMuteMic,
                    onClickJoinLiveChat = onClickJoinLiveChat,
                    onClickChannelChat = onClickChannelChat,
                )
            }
        },
    ) { Unit }

    LaunchedEffect(isVisible) {
        if (isVisible) {
            sheetState.show()
        } else {
            sheetState.hide()
        }
    }
}

@Composable
private fun BottomSheetParticipateContainer(
    modifier: Modifier,
    participantList: List<Any>,
) {
    LazyColumn(modifier = modifier) {
        item {
            ParticipantItem(
                modifier = Modifier
                    .padding(
                        top = 10.dp,
                        bottom = 90.dp,
                        start = 20.dp,
                        end = 20.dp,
                    )
                    .clip(RoundedCornerShape(20.dp))
                    .background(Gray50),
                participantList,
            )
        }
    }
}

@Composable
private fun BottomUtilBarContainer(
    modifier: Modifier,
    onClickMuteMic: () -> Unit,
    onClickJoinLiveChat: () -> Unit,
    onClickChannelChat: () -> Unit,
) {
    Row(
        modifier = modifier.padding(vertical = 5.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        StableImage(
            modifier = Modifier
                .clip(shape = CircleShape)
                .background(Gray50)
                .rippleClick { onClickMuteMic() }
                .size(50.dp)
                .padding(10.dp),
            drawableResId = R.drawable.ic_microphone,
        )
        Text(
            modifier = Modifier
                .padding(horizontal = 5.dp, vertical = 10.dp)
                .clip(shape = RoundedCornerShape(30.dp))
                .background(Color.Green)
                .rippleClick { onClickJoinLiveChat() }
                .padding(horizontal = 70.dp, vertical = 15.dp),
            text = stringResource(R.string.bottom_join_chat),
            textAlign = TextAlign.Center,
            color = White,
        )
        StableImage(
            modifier = Modifier
                .clip(shape = CircleShape)
                .background(Gray50)
                .rippleClick { onClickChannelChat() }
                .size(50.dp)
                .padding(10.dp),
            drawableResId = R.drawable.ic_msg,
        )
    }
}

@Composable
private fun BottomSheetTopContainer(
    modifier: Modifier,
    channelName: String,
    onClickInviteUser: () -> Unit,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            StableImage(
                modifier = Modifier
                    .clip(shape = CircleShape)
                    .background(color = Color.Black)
                    .padding(6.dp)
                    .size(15.dp),
                drawableResId = R.drawable.ic_dropdown_menu,
            )
            ChannelNameContainer(
                modifier = Modifier,
                channelName = channelName,
            )
        }
        StableImage(
            modifier = Modifier
                .clip(shape = CircleShape)
                .background(color = Color.Black)
                .rippleClick { onClickInviteUser() }
                .padding(6.dp)
                .size(15.dp),
            drawableResId = R.drawable.ic_invite_user,
        )
    }
}

@Composable
private fun ChannelNameContainer(modifier: Modifier, channelName: String) {
    Row(
        modifier = modifier
            .padding(start = 10.dp)
            .clip(shape = RoundedCornerShape(15.dp))
            .background(color = Color.Black)
            .padding(horizontal = 10.dp, vertical = 5.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            modifier = Modifier.padding(end = 3.dp),
            text = channelName,
            color = White,
        )
        StableImage(
            modifier = Modifier
                .size(15.dp)
                .rotate(-90f),
            drawableResId = R.drawable.ic_dropdown_menu,
        )
    }
}

@Composable
private fun ParticipantItem(
    modifier: Modifier,
    participantList: List<Any>,
) {
    Column(
        modifier = modifier,
    ) {
        participantList.forEachIndexed { index, participant ->
            DiscordParticipantContainer(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 10.dp),
                userIconResId = 1,
                userName = "",
                userIconSize = 30.dp,
            )
            if (participantList.lastIndex != index) {
                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 5.dp),
                    thickness = 1.dp,
                    color = Gray80,
                )
            }
        }
    }
}

@Composable
@Preview
private fun HomeScreenPreView() {
    HomeScreen(
        onMakeServerClick = {},
        onServerJoinClick = {},
        onClickInviteFriend = {},
        onSearchClick = {},
        uiState = HomeUiState.initialize(),
        onSubChannelClick = {},
        onClickJoinLiveChat = {},
        onClickBackChatRoom = {},
        onServerClick = {},
        onDisMissSheet = {},
        onClickSendChat = {},
    )
}
