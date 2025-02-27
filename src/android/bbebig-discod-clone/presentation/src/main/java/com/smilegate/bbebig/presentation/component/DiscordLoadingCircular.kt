package com.smilegate.bbebig.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.smilegate.bbebig.presentation.theme.MainColor
import com.smilegate.bbebig.presentation.theme.White

@Composable
fun DiscordLoadingCircular(modifier: Modifier = Modifier, backgroundColor: Color = White) {
    Box(modifier = modifier.background(backgroundColor), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(
            modifier = Modifier.align(Alignment.Center),
            color = MainColor,
        )
    }
}

@Composable
@Preview
private fun SoopLoadingCircularPreview() {
    DiscordLoadingCircular(Modifier.fillMaxSize())
}
