package com.smilegate.bbebig.presentation.ui.signup

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
import androidx.compose.ui.text.input.KeyboardType
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
fun PhoneNumberScreen(
    modifier: Modifier,
    onBackClick: () -> Unit,
    onClickConfirm: (Int) -> Unit,
) {
    val phoneNumberState = rememberTextFieldState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        DiscordTopBar(
            modifier = Modifier.fillMaxWidth(),
            onBackClick = onBackClick,
            backButtonColor = White,
        )
        Text(
            modifier = Modifier.padding(top = 20.dp),
            text = stringResource(R.string.phone_numer_title),
            fontSize = 24.sp,
            textAlign = TextAlign.Center,
        )
        DiscordInputContainer(
            modifier = Modifier.padding(top = 20.dp),
            textState = phoneNumberState,
            titleResId = R.string.phone_number,
            textHintResId = R.string.phone_number,
            keyboardType = KeyboardType.NumberPassword,
        )
        DiscordRoundButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 15.dp),
            textResId = R.string.auth,
            backgroundColor = Blue70,
            onClick = {
                onClickConfirm(
                    phoneNumberState.text.toString().toInt(),
                )
            },
            textColor = White,
            radiusDp = 5.dp,
            isEnable = phoneNumberState.text.isNotEmpty(),
        )
    }
}

@Composable
@Preview
private fun PreviewPhoneNumberScreen() {
    PhoneNumberScreen(
        modifier = Modifier,
        onBackClick = {},
        onClickConfirm = {},
    )
}
