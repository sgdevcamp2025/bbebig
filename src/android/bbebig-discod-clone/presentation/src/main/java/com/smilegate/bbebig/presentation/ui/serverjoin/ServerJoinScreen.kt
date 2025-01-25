package com.smilegate.bbebig.presentation.ui.serverjoin

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.smilegate.bbebig.presentation.component.DiscordInputContainer
import com.smilegate.bbebig.presentation.component.DiscordRoundButton
import com.smilegate.bbebig.presentation.component.DiscordTitleContainer
import com.smilegate.bbebig.presentation.component.DiscordTopBar
import com.smilegate.bbebig.presentation.theme.Blue70
import com.smilegate.bbebig.presentation.theme.White
import com.smilegate.devcamp.presentation.R

@Composable
fun ServerJoinScreen(onClickBack: () -> Unit, onClickServerJoin: (String) -> Unit) {
    val linkTextState = rememberTextFieldState()

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
                firstTitleResId = R.string.server_join_title,
                secondTitleResId = R.string.server_join_sub_title,
            )
            DiscordInputContainer(
                modifier = Modifier.fillMaxWidth(),
                textState = linkTextState,
                titleResId = R.string.server_join_link_title,
                textHintResId = R.string.server_join_hint,
            )
            Text(
                modifier = Modifier.padding(top = 10.dp),
                text = stringResource(R.string.server_join_how_input_link),
            )
        }
        DiscordRoundButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.dp, end = 10.dp, bottom = 20.dp)
                .align(Alignment.BottomCenter),
            textResId = R.string.server_join_btn_text,
            textColor = White,
            backgroundColor = Blue70,
            onClick = { onClickServerJoin(linkTextState.text.toString()) },
        )
    }
}

@Composable
@Preview
private fun ServerJoinScreenPreview() {
    ServerJoinScreen({}, {})
}
