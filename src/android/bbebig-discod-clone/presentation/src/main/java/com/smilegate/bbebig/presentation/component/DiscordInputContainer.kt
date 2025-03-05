package com.smilegate.bbebig.presentation.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.smilegate.bbebig.presentation.theme.Gray40

@Composable
fun DiscordInputContainer(
    modifier: Modifier,
    textState: TextFieldState,
    titleResId: Int,
    textHintResId: Int,
    isPasswordType: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text,
) {
    Column(modifier = modifier) {
        Text(
            modifier = Modifier.padding(bottom = 5.dp),
            text = stringResource(titleResId),
            color = Gray40,
            textAlign = TextAlign.Center,
        )
        DiscordTextField(
            modifier = Modifier
                .fillMaxWidth(),
            textFieldState = textState,
            textHint = stringResource(textHintResId),
            keyboardType = keyboardType,
            isPasswordType = isPasswordType,
        )
    }
}
