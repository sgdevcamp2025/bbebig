package com.smilegate.bbebig.presentation.ui.signup.authcode

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smilegate.bbebig.presentation.component.DiscordRoundButton
import com.smilegate.bbebig.presentation.component.DiscordTextField
import com.smilegate.bbebig.presentation.component.DiscordTopBar
import com.smilegate.bbebig.presentation.theme.Blue70
import com.smilegate.bbebig.presentation.theme.White
import com.smilegate.devcamp.presentation.R

@Composable
fun AuthCodeScreen(
    onBackClick: () -> Unit,
    onClickConfirm: (Int) -> Unit,
) {
    val codeState = rememberTextFieldState()

    Column(
        modifier = Modifier.padding(horizontal = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        DiscordTopBar(
            modifier = Modifier.fillMaxWidth(),
            onBackClick = onBackClick,
            backButtonColor = White,
        )
        Text(
            modifier = Modifier.padding(top = 20.dp),
            text = stringResource(R.string.auth_code_title),
            fontSize = 24.sp,
            textAlign = TextAlign.Center,
        )
        DiscordTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 15.dp),
            textFieldState = codeState,
            keyboardType = KeyboardType.Phone,
        )
        DiscordRoundButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 15.dp),
            textResId = R.string.auth,
            backgroundColor = Blue70,
            onClick = {
                onClickConfirm(codeState.text.toString().toInt())
            },
            textColor = White,
            radiusDp = 5.dp,
            isEnable = codeState.text.isNotEmpty(),
        )
    }
}

@Composable
@Preview
private fun AuthCodeScreenPreview() {
    AuthCodeScreen(
        onBackClick = {},
        onClickConfirm = {},
    )
}
