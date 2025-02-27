package com.smilegate.bbebig.presentation.ui.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smilegate.bbebig.presentation.component.DiscordLoadingCircular
import com.smilegate.bbebig.presentation.component.DiscordRoundButton
import com.smilegate.bbebig.presentation.component.DiscordTextField
import com.smilegate.bbebig.presentation.component.DiscordTopBar
import com.smilegate.bbebig.presentation.theme.Blue70
import com.smilegate.bbebig.presentation.theme.Gray20
import com.smilegate.bbebig.presentation.theme.Gray90
import com.smilegate.bbebig.presentation.ui.login.mvi.LoginUiState
import com.smilegate.devcamp.presentation.R

@Composable
fun LoginScreen(
    modifier: Modifier,
    uiState: LoginUiState,
    onClickLoginConfirm: (String, String) -> Unit,
    onBackClick: () -> Unit,
) {
    Column(
        modifier = modifier
            .background(Gray20)
            .fillMaxSize()
            .padding(horizontal = 10.dp),
    ) {
        DiscordTopBar(
            modifier = Modifier.fillMaxWidth(),
            onBackClick = onBackClick,
            backButtonColor = Gray20,
        )
        TitleContainer(
            modifier = Modifier
                .padding(top = 20.dp)
                .wrapContentSize()
                .align(Alignment.CenterHorizontally),
        )
        Text(
            modifier = Modifier.padding(top = 20.dp),
            text = stringResource(R.string.account_info_title),
            color = Gray90,
        )
        InputAccountContainer(
            modifier = Modifier.padding(top = 8.dp),
            onClickLoginConfirm = onClickLoginConfirm,
        )
    }

    if (uiState.isLoading) {
        DiscordLoadingCircular(modifier = Modifier.fillMaxSize())
    }
}

@Composable
private fun TitleContainer(modifier: Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(R.string.login_welcome_title),
            fontSize = 20.sp,
            color = Gray90,
            fontWeight = FontWeight.Bold,
        )
        Text(
            modifier = Modifier.padding(top = 15.dp),
            text = stringResource(R.string.login_welcome_sub_title),
            color = Gray90,
        )
    }
}

@Composable
private fun InputAccountContainer(
    modifier: Modifier,
    onClickLoginConfirm: (String, String) -> Unit,
) {
    val emailState = rememberTextFieldState()
    val passwordState = rememberTextFieldState()

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(5.dp),
    ) {
        DiscordTextField(
            modifier = Modifier.fillMaxWidth(),
            textFieldState = emailState,
            textHint = stringResource(R.string.email_pw_hint),
            isPasswordType = false,
        )

        DiscordTextField(
            modifier = Modifier.fillMaxWidth(),
            textFieldState = passwordState,
            textHint = stringResource(R.string.pw_hint),
            isPasswordType = true,
        )
    }

    DiscordRoundButton(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp),
        textResId = R.string.login,
        backgroundColor = Blue70,
        onClick = {
            onClickLoginConfirm(emailState.text.toString(), passwordState.text.toString())
        },
        textColor = Color.White,
        radiusDp = 5.dp,
    )
}

@Composable
@Preview
private fun LoginScreenPreview() {
    LoginScreen(
        modifier = Modifier.fillMaxSize(),
        onBackClick = {},
        onClickLoginConfirm = { _, _ -> },
        uiState = LoginUiState.init(),
    )
}
