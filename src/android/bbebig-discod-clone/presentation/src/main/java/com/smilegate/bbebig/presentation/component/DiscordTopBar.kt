package com.smilegate.bbebig.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smilegate.bbebig.presentation.theme.Gray50
import com.smilegate.bbebig.presentation.theme.White
import com.smilegate.bbebig.presentation.utils.StableImage
import com.smilegate.bbebig.presentation.utils.noRippleSingleClick
import com.smilegate.bbebig.presentation.utils.rippleSingleClick
import com.smilegate.devcamp.presentation.R

@Composable
fun DiscordTopBar(
    modifier: Modifier,
    backButtonColor: Color,
    onBackClick: () -> Unit,
    title: String = "",
    onClickSearch: () -> Unit = {},
) {
    Box(
        modifier = modifier
            .background(color = Color.Transparent)
            .drawBehind {
                if (title.isEmpty()) return@drawBehind
                val strokeWidth = 1.dp.toPx()
                val y = size.height - strokeWidth / 2
                drawLine(
                    color = Color.Gray,
                    start = Offset(0f, y),
                    end = Offset(size.width, y),
                    strokeWidth = strokeWidth,
                )
            },
    ) {
        Row(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(vertical = 15.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            BackButton(
                modifier = Modifier
                    .wrapContentSize()
                    .padding(start = 16.dp),
                backButtonColor = backButtonColor,
                onBackClick = onBackClick,
            )
            if (title.isNotEmpty()) {
                Text(
                    modifier = Modifier.padding(start = 16.dp),
                    text = title,
                    fontSize = 20.sp,
                    color = White,
                )
                StableImage(
                    modifier = Modifier
                        .size(20.dp)
                        .padding(start = 4.dp)
                        .rotate(-90f),
                    drawableResId = R.drawable.ic_dropdown_menu,
                )
            }
        }

        if (title.isNotEmpty()) {
            StableImage(
                modifier = Modifier
                    .padding(end = 10.dp)
                    .clip(CircleShape)
                    .background(color = Gray50)
                    .size(30.dp)
                    .rippleSingleClick { onClickSearch() }
                    .padding(7.dp)
                    .align(Alignment.CenterEnd),
                drawableResId = R.drawable.ic_search,
            )
        }
    }
}

@Composable
private fun BackButton(
    modifier: Modifier,
    backButtonColor: Color,
    onBackClick: () -> Unit,
) {
    StableImage(
        modifier = modifier.noRippleSingleClick { onBackClick() },
        colorFilter = ColorFilter.tint(backButtonColor),
        drawableResId = R.drawable.ic_back,
        contentDescription = stringResource(R.string.desc_back),
    )
}

@Composable
@Preview
private fun DiscordTopBarPreview() {
    DiscordTopBar(
        modifier = Modifier.fillMaxWidth(),
        backButtonColor = White,
        title = "Title",
        onBackClick = {},
    )
}
