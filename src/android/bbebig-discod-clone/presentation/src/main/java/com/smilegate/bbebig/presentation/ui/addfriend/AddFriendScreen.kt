package com.smilegate.bbebig.presentation.ui.addfriend

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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smilegate.bbebig.presentation.component.DiscordInputContainer
import com.smilegate.bbebig.presentation.component.DiscordRoundButton
import com.smilegate.bbebig.presentation.component.DiscordTopBar
import com.smilegate.bbebig.presentation.theme.Blue70
import com.smilegate.bbebig.presentation.theme.White
import com.smilegate.devcamp.presentation.R

@Composable
fun AddFriendScreen(
    onClickBack: () -> Unit,
    onClickAddFriend: (String) -> Unit,
) {
    val idTextState = rememberTextFieldState()

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
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 40.dp, bottom = 20.dp),
                text = stringResource(R.string.add_friend_title),
                fontSize = 26.sp,
                color = White,
                textAlign = TextAlign.Center,
            )
            DiscordInputContainer(
                modifier = Modifier.fillMaxWidth(),
                textState = idTextState,
                titleResId = R.string.add_friend_input_title,
                textHintResId = R.string.add_friend_hint,
            )
            Text(
                modifier = Modifier.padding(top = 10.dp),
                // TODO: 향후 사용자 인자가 들어가야 됨
                text = "그건 그렇고 내 사용자명은?",
            )
        }
        DiscordRoundButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.dp, end = 10.dp, bottom = 20.dp)
                .align(Alignment.BottomCenter),
            textResId = R.string.add_friend_request,
            textColor = White,
            backgroundColor = Blue70,
            onClick = { onClickAddFriend(idTextState.text.toString()) },
        )
    }
}

@Composable
@Preview
private fun FriendAddScreenPreview() {
    AddFriendScreen({}, {})
}
