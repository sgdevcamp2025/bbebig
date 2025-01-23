package com.smilegate.bbebig.presentation.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smilegate.bbebig.presentation.theme.Blue70
import com.smilegate.bbebig.presentation.theme.White
import com.smilegate.bbebig.presentation.utils.StableImage
import com.smilegate.bbebig.presentation.utils.rippleSingleClick
import com.smilegate.devcamp.presentation.R

@Composable
fun DiscordRoundButton(
    modifier: Modifier,
    textResId: Int,
    backgroundColor: Color,
    onClick: () -> Unit,
    textSize: TextUnit = 15.sp,
    iconSize: Dp = 15.dp,
    textColor: Color = Color.Black,
    verticalInnerPadding: Dp = 13.dp,
    @DrawableRes drawableRedId: Int? = null,
    radiusDp: Dp = 50.dp,
    isEnable: Boolean = true,
) {
    Row(
        modifier = modifier
            .clip(shape = RoundedCornerShape(radiusDp))
            .rippleSingleClick { if (isEnable) onClick() }
            .background(color = if (isEnable) backgroundColor else backgroundColor.copy(alpha = 0.5f))
            .padding(vertical = verticalInnerPadding)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        drawableRedId?.let {
            StableImage(
                modifier = Modifier
                    .padding(end = 5.dp)
                    .size(iconSize),
                drawableResId = it,
                contentScale = ContentScale.Fit,
                contentDescription = null,
            )
        }
        Text(
            text = stringResource(id = textResId),
            color = textColor,
            textAlign = TextAlign.Center,
            fontSize = textSize,
        )
    }
}

@Composable
@Preview
private fun DiscordRoundButtonPreview() {
    DiscordRoundButton(
        modifier = Modifier,
        textResId = R.string.server_join_text,
        backgroundColor = Blue70,
        drawableRedId = R.drawable.ic_dropdown_menu,
        textColor = White,
        onClick = {},
    )
}
