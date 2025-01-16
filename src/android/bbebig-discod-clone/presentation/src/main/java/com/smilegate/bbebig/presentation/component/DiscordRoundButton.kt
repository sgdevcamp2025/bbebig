package com.smilegate.bbebig.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.smilegate.bbebig.presentation.utils.rippleSingleClick

@Composable
fun DiscordRoundButton(
    modifier: Modifier,
    textResId: Int,
    backgroundColor: Color,
    onClick: () -> Unit,
    textColor: Color = Color.Black,
    radiusDp: Dp = 50.dp,
) {
    Text(
        modifier = modifier
            .clip(shape = RoundedCornerShape(radiusDp))
            .rippleSingleClick { onClick() }
            .background(color = backgroundColor)
            .padding(vertical = 13.dp)
            .fillMaxWidth(),
        text = stringResource(id = textResId),
        color = textColor,
        textAlign = TextAlign.Center,
    )
}
