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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.smilegate.bbebig.presentation.component.DiscordRoundButton
import com.smilegate.bbebig.presentation.model.SampleChannel
import com.smilegate.bbebig.presentation.model.SampleServerList
import com.smilegate.bbebig.presentation.theme.Blue70
import com.smilegate.bbebig.presentation.theme.Gray30
import com.smilegate.bbebig.presentation.theme.Gray50
import com.smilegate.bbebig.presentation.theme.Gray60
import com.smilegate.bbebig.presentation.theme.Gray70
import com.smilegate.bbebig.presentation.theme.Gray80
import com.smilegate.bbebig.presentation.theme.White
import com.smilegate.bbebig.presentation.utils.StableImage
import com.smilegate.bbebig.presentation.utils.noRippleSingleClick
import com.smilegate.bbebig.presentation.utils.rippleSingleClick
import com.smilegate.devcamp.presentation.R

@Composable
fun HomeScreen(
    onMakeServerClick: () -> Unit,
    onServerJoinClick: () -> Unit,
    onSearchClick: () -> Unit = {},
    serverList: SampleServerList = SampleServerList.getDummyList(),
) {
    Row(
        modifier = Modifier.fillMaxSize(),
    ) {
        ServerListContainer(
            modifier = Modifier.fillMaxHeight(),
            serverList = serverList,
            onMakeServerClick = onMakeServerClick,
        )
        ServerChannelContainer(
            modifier = Modifier.background(color = Gray30),
            serverList = serverList,
            onMakeServerClick = onMakeServerClick,
            onServerJoinClick = onServerJoinClick,
            onSearchClick = onSearchClick,
        )
    }
}

@Composable
private fun ServerListContainer(
    modifier: Modifier,
    serverList: SampleServerList,
    onMakeServerClick: () -> Unit,
) {
    val selectedServerId = remember { mutableIntStateOf(Int.MIN_VALUE) }

    LazyColumn(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        itemsIndexed(serverList.channelList) { index, serverInfo ->
            if (index == 0) {
                Spacer(modifier = Modifier.height(10.dp))
            }
            ServerItem(
                modifier = Modifier,
                isSelected = selectedServerId.intValue == index,
                index = index,
                imageUrl = "https://picsum.photos/seed/picsum/200/300",
                onServerClick = { clickedIndex ->
                    selectedServerId.intValue = clickedIndex
                },
            )
            if (index == 0) {
                HorizontalDivider(
                    modifier = Modifier
                        .width(25.dp)
                        .padding(top = 10.dp),
                    thickness = 1.dp,
                    color = Gray80,
                )
            }
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
        TitleContainer(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(vertical = 20.dp),
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
private fun TitleContainer(modifier: Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Text(stringResource(R.string.empty_title), textAlign = TextAlign.Center)
        Text(stringResource(R.string.empty_sub_title), textAlign = TextAlign.Center)
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
    serverList: SampleServerList,
    onMakeServerClick: () -> Unit,
    onServerJoinClick: () -> Unit,
    onSearchClick: () -> Unit,
) {
    if (serverList.channelList.isEmpty()) {
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
            serverList = serverList,
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ServerContentContainer(
    modifier: Modifier,
    serverList: SampleServerList,
    onSearchClick: () -> Unit,
) {
    LazyColumn(modifier = modifier) {
        stickyHeader {
            Column(modifier = modifier.padding(horizontal = 15.dp)) {
                TopTitleContainer(modifier = Modifier, title = serverList.serverName, onSearchClick)
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

        itemsIndexed(serverList.channelList) { index, serverInfo ->
            ChannelItem(
                modifier = Modifier.padding(vertical = 5.dp),
                channelInfo = serverInfo,
                onChanelClick = { },
                onSubChannelClick = { },
            )
        }
    }
}

@Composable
private fun ChannelItem(
    modifier: Modifier,
    channelInfo: SampleChannel,
    onChanelClick: () -> Unit,
    onSubChannelClick: () -> Unit,
) {
    var isChannelSelected by rememberSaveable { mutableStateOf(false) }

    Column(modifier = modifier) {
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
            Text(text = channelInfo.channelName)
        }

        if (isChannelSelected) {
            Column(
                modifier = Modifier.padding(start = 15.dp, top = 5.dp, bottom = 5.dp),
            ) {
                channelInfo.subChannelList.forEach { subChannel ->
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 3.dp)
                            .rippleSingleClick { /*TODO: 향후 채널 선택시 클릭이벤트 적용*/ },
                        text = subChannel.subChannelName,
                    )
                }
            }
        }
    }
}

@Composable
@Preview
private fun ServerItemPreview() {
    HomeScreen(
        onMakeServerClick = {},
        onServerJoinClick = {},
    )
}
