package com.smilegate.bbebig.presentation.ui.livechat

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smilegate.bbebig.presentation.theme.Gray40
import com.smilegate.bbebig.presentation.theme.Gray50
import com.smilegate.bbebig.presentation.theme.White
import com.smilegate.bbebig.presentation.utils.StableImage
import com.smilegate.bbebig.presentation.utils.noRippleClick
import com.smilegate.bbebig.presentation.utils.rippleClick
import com.smilegate.devcamp.presentation.R

@Composable
fun LiveChatScreen(
    title: String,
    isVideoChat: Boolean,
    participants: List<Any>,
    onClickPopUp: () -> Unit,
    onClickMute: () -> Unit,
    onClickSwitchCamera: () -> Unit,
    onClickMessageChat: () -> Unit,
) {
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp

//    val sessionManager = LocalWebRtcSessionManager.current
//
//    var parentSize: IntSize by remember { mutableStateOf(IntSize(0, 0)) }
//
//    val remoteVideoTrackState by sessionManager.remoteVideoTrackFlow.collectAsState(null)
//    val remoteVideoTrack = remoteVideoTrackState
//
//    val localVideoTrackState by sessionManager.localVideoTrackFlow.collectAsState(null)
//    val localVideoTrack = localVideoTrackState
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        LiveChatTopBar(
            modifier = Modifier
                .padding(top = 30.dp, end = 10.dp)
                .fillMaxWidth(),
            onClickPopUp = onClickPopUp,
            title = title,
            isVideoChat = isVideoChat,
        )
        LiveChatParticipantContainer(
            modifier = Modifier
                .padding(
                    horizontal = 20.dp,
                    vertical = 30.dp,
                )
                .heightIn(max = screenHeight - 220.dp)
                .widthIn(max = screenWidth - 60.dp),
            // TODO: Replace with actual list
            participants = participants,
        )
//        VideoRenderer(
//            videoTrack = remoteVideoTrack ?: localVideoTrack ?: return,
//            modifier = Modifier
//                .fillMaxSize()
//                .onSizeChanged { parentSize = it }
//        )
//        ChatToolContainer(
//            modifier = Modifier
//                .padding(start = 10.dp, end = 10.dp, bottom = 5.dp)
//                .fillMaxWidth()
//                .clip(shape = RoundedCornerShape(25.dp))
//                .background(Gray40),
//            onClickMute = onClickMute,
//            onClickSwitchCamera = onClickSwitchCamera,
//            onClickMessageChat = onClickMessageChat,
//        )
    }
}

@Composable
private fun LiveChatParticipantContainer(modifier: Modifier, participants: List<Any>) {
    LazyHorizontalGrid(
        modifier = modifier,
        rows = GridCells.Fixed(if (participants.size < 3) 2 else if (participants.size <= 9) 3 else 4),
        horizontalArrangement = Arrangement.spacedBy(3.dp),
        verticalArrangement = Arrangement.spacedBy(3.dp),
    ) {
        // TODO: Replace with actual data
        itemsIndexed(participants) { index, participant ->
            ParticipantItem(
                modifier = Modifier,
                isTalking = index == 0,
                userThumbnailRedId = R.drawable.ic_meg_send,
                userName = "User $index",
            )
        }
    }
}

@Composable
private fun LiveChatTopBar(
    modifier: Modifier,
    title: String,
    isVideoChat: Boolean,
    onClickPopUp: () -> Unit,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Row(
            modifier = Modifier,
        ) {
            StableImage(
                modifier = Modifier
                    .padding(start = 10.dp, end = 30.dp)
                    .size(15.dp),
                drawableResId = R.drawable.ic_dropdown_menu,
            )
            Text(
                modifier = Modifier.padding(end = 5.dp),
                text = title,
                color = White,
            )
            StableImage(
                modifier = Modifier
                    .size(15.dp)
                    .rotate(-90f),
                drawableResId = R.drawable.ic_dropdown_menu,
            )
        }
        Row(
            modifier = Modifier,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            StableImage(
                modifier = Modifier
                    .clip(shape = CircleShape)
                    .background(White)
                    .noRippleClick { onClickPopUp() }
                    .size(30.dp)
                    .padding(7.dp),
                drawableResId = R.drawable.ic_speaker,
                colorFilter = ColorFilter.tint(color = Color.Black),
            )

            if (isVideoChat) {
                StableImage(
                    modifier = Modifier
                        .clip(shape = CircleShape)
                        .background(Color.Black)
                        .size(30.dp)
                        .padding(7.dp),
                    drawableResId = R.drawable.ic_switch_camera,
                    colorFilter = ColorFilter.tint(color = White),
                )
            }
        }
    }
}

@Composable
private fun ChatToolContainer(
    modifier: Modifier,
    onClickMessageChat: () -> Unit,
    onClickMute: () -> Unit,
    onClickSwitchCamera: () -> Unit,
) {
    Row(
        modifier = modifier.padding(vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        StableImage(
            modifier = Modifier
                .clip(shape = CircleShape)
                .background(Gray50)
                .rippleClick { onClickMessageChat() }
                .size(50.dp)
                .padding(10.dp),
            drawableResId = R.drawable.ic_video,
        )
        StableImage(
            modifier = Modifier
                .clip(shape = CircleShape)
                .background(Gray50)
                .rippleClick { onClickMessageChat() }
                .size(50.dp)
                .padding(10.dp),
            drawableResId = R.drawable.ic_microphone,
        )
        StableImage(
            modifier = Modifier
                .clip(shape = CircleShape)
                .background(Gray50)
                .rippleClick { onClickMute() }
                .size(50.dp)
                .padding(10.dp),
            drawableResId = R.drawable.ic_msg,
        )
        StableImage(
            modifier = Modifier
                .clip(shape = CircleShape)
                .background(Gray50)
                .rippleClick { onClickSwitchCamera() }
                .size(50.dp)
                .padding(10.dp),
            drawableResId = R.drawable.ic_disconnect,
        )
    }
}

@Composable
private fun ParticipantItem(
    modifier: Modifier,
    isTalking: Boolean,
    userThumbnailRedId: Int,
    userName: String,
) {
    Box(
        modifier = modifier
            .aspectRatio(1f)
            .clip(shape = RoundedCornerShape(15.dp))
            .border(
                width = 1.dp,
                color = if (isTalking) Color.Green else Color.Transparent,
                shape = RoundedCornerShape(15.dp),
            )
            .background(Gray50, shape = RoundedCornerShape(15.dp)),
    ) {
        StableImage(
            modifier = Modifier
                .clip(shape = CircleShape)
                .size(50.dp)
                .align(Alignment.Center),
            drawableResId = userThumbnailRedId,
        )
        Text(
            modifier = Modifier
                .padding(bottom = 5.dp)
                .clip(shape = RoundedCornerShape(15.dp))
                .background(Color.Black.copy(alpha = 0.5f))
                .padding(horizontal = 8.dp, vertical = 3.dp)
                .align(Alignment.BottomCenter),
            text = userName,
            fontSize = 10.sp,
            color = White,
        )
    }
}

@Composable
@Preview
private fun LiveChatScreenPreview() {
    LiveChatScreen(
        title = "Title",
        isVideoChat = true,
        participants = listOf(Any(), Any(), Any(), Any(), Any(), Any(), Any(), Any(), Any(), Any()),
        onClickPopUp = {},
        onClickMute = {},
        onClickSwitchCamera = {},
        onClickMessageChat = {},
    )
}
