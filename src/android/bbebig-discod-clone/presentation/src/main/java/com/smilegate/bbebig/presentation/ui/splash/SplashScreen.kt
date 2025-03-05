package com.smilegate.bbebig.presentation.ui.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.smilegate.bbebig.presentation.theme.Gray15
import com.smilegate.bbebig.presentation.utils.StableImage
import com.smilegate.devcamp.presentation.R

@Composable
fun SplashScreen(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = Gray15),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            StableImage(
                modifier = Modifier.size(150.dp),
                drawableResId = R.drawable.ic_logo,
            )
            StableImage(
                modifier = Modifier.size(height = 50.dp, width = 150.dp),
                drawableResId = R.drawable.ic_brand_label,
            )
        }
    }
}

@Preview
@Composable
private fun SplashScreenPreview() {
    SplashScreen()
}
