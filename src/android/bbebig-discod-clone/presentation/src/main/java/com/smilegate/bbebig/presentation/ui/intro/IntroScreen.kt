package com.smilegate.bbebig.presentation.ui.intro

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.smilegate.bbebig.presentation.component.DiscordRoundButton
import com.smilegate.bbebig.presentation.theme.Blue50
import com.smilegate.bbebig.presentation.theme.Gray20
import com.smilegate.bbebig.presentation.theme.Gray90
import com.smilegate.bbebig.presentation.theme.White
import com.smilegate.devcamp.presentation.R

@Composable
fun IntroScreen(
    modifier: Modifier,
    onNavigateToLoginClick: () -> Unit,
    onNavigateToSignUpClick: () -> Unit,
) {
    Box(
        modifier = modifier
            .background(Gray20)
            .fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        IntroTitleContainer(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 150.dp),
        )
        ButtonGroupContainer(
            modifier = Modifier.align(Alignment.BottomCenter),
            onNavigateToLoginClick = onNavigateToLoginClick,
            onNavigateToSignUpClick = onNavigateToSignUpClick,
        )
    }
}

@Composable
private fun IntroTitleContainer(modifier: Modifier) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        AsyncImage(
            modifier = Modifier.width(200.dp).height(150.dp),
            model = R.drawable.ic_logo,
            contentScale = ContentScale.Crop,
            contentDescription = "Discord Logo",
        )
        Text(
            text = stringResource(R.string.intro_title),
            textAlign = TextAlign.Center,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = Gray90,
        )
        Text(
            text = stringResource(R.string.intro_sub_title),
            textAlign = TextAlign.Center,
            color = Gray90,
        )
    }
}

@Composable
private fun ButtonGroupContainer(
    modifier: Modifier,
    onNavigateToLoginClick: () -> Unit,
    onNavigateToSignUpClick: () -> Unit,
) {
    Column(
        modifier = modifier
            .wrapContentSize()
            .padding(horizontal = 40.dp),
    ) {
        DiscordRoundButton(
            modifier = Modifier,
            textResId = R.string.sign_up,
            backgroundColor = White,
            onClick = onNavigateToSignUpClick,
        )
        DiscordRoundButton(
            modifier = Modifier.padding(vertical = 15.dp),
            textResId = R.string.login,
            textColor = White,
            backgroundColor = Blue50,
            onClick = onNavigateToLoginClick,
        )
    }
}

@Composable
@Preview
private fun IntroPreview() {
    IntroScreen(
        modifier = Modifier.fillMaxSize(),
        onNavigateToLoginClick = {},
        onNavigateToSignUpClick = {},
    )
}
