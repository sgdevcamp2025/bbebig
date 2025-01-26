package com.smilegate.bbebig.presentation.ui.createserver

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.smilegate.bbebig.presentation.component.DiscordInputContainer
import com.smilegate.bbebig.presentation.component.DiscordRoundButton
import com.smilegate.bbebig.presentation.component.DiscordTitleContainer
import com.smilegate.bbebig.presentation.component.DiscordTopBar
import com.smilegate.bbebig.presentation.theme.Blue70
import com.smilegate.bbebig.presentation.theme.White
import com.smilegate.bbebig.presentation.utils.StableImage
import com.smilegate.bbebig.presentation.utils.rippleSingleClick
import com.smilegate.devcamp.presentation.R

@Composable
fun CreateServerScreen(
    onClickBack: () -> Unit,
    onClickCreateServer: (String) -> Unit,
    onClickAddThumbnail: () -> Unit,
) {
    val serverNameTextState = rememberTextFieldState()

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 10.dp)
                .align(Alignment.TopCenter),
        ) {
            DiscordTopBar(
                modifier = Modifier.fillMaxWidth(),
                backButtonColor = White,
                onBackClick = onClickBack,
            )
            DiscordTitleContainer(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 20.dp),
                firstTitleResId = R.string.create_server_title,
                secondTitleResId = R.string.create_server_sub_title,
            )
            ServerThumbnail(
                modifier = Modifier
                    .padding(vertical = 10.dp)
                    .align(Alignment.CenterHorizontally),
                onClickAddThumbnail = onClickAddThumbnail,
            )
            DiscordInputContainer(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                textState = serverNameTextState,
                titleResId = R.string.create_server_input_title,
                textHintResId = R.string.create_server_hint,
            )
            DiscordRoundButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp),
                textResId = R.string.create_server_btn,
                textColor = White,
                backgroundColor = Blue70,
                onClick = { onClickCreateServer(serverNameTextState.text.toString()) },
            )
        }
    }
}

@Composable
private fun ServerThumbnail(modifier: Modifier, onClickAddThumbnail: () -> Unit) {
    StableImage(
        modifier = modifier
            .size(70.dp)
            .clip(
                shape = CircleShape,
            )
            .background(
                color = White,
            )
            .rippleSingleClick { onClickAddThumbnail() }
            .drawBehind {
                val borderWidth = 2.dp.toPx() // 테두리 두께
                val radius = size.minDimension / 2 // 원의 반지름
                val dashEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f) // 점선 효과

                drawCircle(
                    color = Color.Black, // 점선 색상
                    radius = radius - borderWidth / 2, // 테두리가 이미지 내부로 잘리지 않도록 조정
                    style = Stroke(
                        width = borderWidth,
                        pathEffect = dashEffect, // 점선 효과 적용
                    ),
                )
            }
            .padding(15.dp),

        drawableResId = R.drawable.ic_plus,
    )
}

@Composable
@Preview
private fun CreateServerScreenPreview() {
    CreateServerScreen(
        {},
        {},
    ) {}
}
