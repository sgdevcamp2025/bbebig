package com.smilegate.bbebig.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.smilegate.bbebig.presentation.utils.StableImage
import com.smilegate.bbebig.presentation.utils.noRippleSingleClick
import com.smilegate.devcamp.presentation.R

@Composable
fun DiscordTopBar(
    modifier: Modifier,
    backButtonColor: Color,
    onBackClick: () -> Unit,
) {
    Box(
        modifier = modifier.background(color = Color.Transparent),
    ) {
        BackButton(
            modifier = Modifier
                .wrapContentSize()
                .padding(top = 12.dp, start = 16.dp)
                .align(Alignment.CenterStart),
            backButtonColor = backButtonColor,
            onBackClick = onBackClick,
        )
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
