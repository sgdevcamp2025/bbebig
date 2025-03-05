package com.smilegate.bbebig.presentation.ui.signup.account

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
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
import com.smilegate.bbebig.presentation.theme.Gray20
import com.smilegate.bbebig.presentation.theme.Gray90
import com.smilegate.bbebig.presentation.theme.White
import com.smilegate.bbebig.presentation.ui.signup.account.mvi.InputAccountUiState
import com.smilegate.devcamp.presentation.R

@Composable
fun AccountScreen(
    uiState: InputAccountUiState,
    onBackClick: () -> Unit,
    onClickConfirm: (String, String) -> Unit,
) {
    val userNameState = rememberTextFieldState()
    val passwordState = rememberTextFieldState()

    Column(
        modifier = Modifier.background(Gray20).padding(horizontal = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        DiscordTopBar(
            modifier = Modifier.statusBarsPadding().fillMaxWidth(),
            onBackClick = onBackClick,
            backButtonColor = Gray90,
        )
        Text(
            modifier = Modifier.padding(top = 20.dp),
            text = stringResource(R.string.user_info_title),
            fontSize = 24.sp,
            textAlign = TextAlign.Center,
            color = Gray90,
        )
        DiscordInputContainer(
            modifier = Modifier.padding(top = 20.dp),
            textState = userNameState,
            titleResId = R.string.username,
            textHintResId = R.string.username,
        )
        DiscordInputContainer(
            modifier = Modifier.padding(top = 20.dp),
            textState = passwordState,
            titleResId = R.string.pw_hint,
            textHintResId = R.string.pw_hint,
            isPasswordType = true,
        )
        PasswordIntroContainer(
            modifier = Modifier.fillMaxWidth(),
        )
        DiscordRoundButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp),
            textResId = R.string.next,
            backgroundColor = Blue70,
            onClick = {
                onClickConfirm(
                    userNameState.text.toString(),
                    passwordState.text.toString(),
                )
            },
            textColor = White,
            radiusDp = 5.dp,
            isEnable = userNameState.text.isNotEmpty() && passwordState.text.isNotEmpty(),
        )
    }
}

@Composable
fun PasswordIntroContainer(modifier: Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            modifier = Modifier.padding(top = 5.dp),
            text = stringResource(R.string.require_pw),
            textAlign = TextAlign.Center,
            color = Gray90,
        )
    }
}

@Composable
@Preview
private fun UserInfoScreenPreview() {
    AccountScreen(
        uiState = InputAccountUiState.initial(),
        onBackClick = {},
        onClickConfirm = { _, _ -> },
    )
}
