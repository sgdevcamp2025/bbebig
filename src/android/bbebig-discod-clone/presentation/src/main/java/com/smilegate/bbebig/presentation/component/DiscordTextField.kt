package com.smilegate.bbebig.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.OutputTransformation
import androidx.compose.foundation.text.input.TextFieldBuffer
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smilegate.bbebig.presentation.theme.Gray25
import com.smilegate.bbebig.presentation.theme.Gray40
import com.smilegate.bbebig.presentation.theme.Gray90

@Composable
fun DiscordTextField(
    modifier: Modifier,
    textFieldState: TextFieldState,
    textHint: String = "",
    isSingLine: Boolean = true,
    keyboardType: KeyboardType = KeyboardType.Text,
    radius: Dp = 8.dp,
    isPasswordType: Boolean = false,
) {
    BasicTextField(
        modifier = modifier,
        state = textFieldState,
        lineLimits = if (isSingLine) TextFieldLineLimits.SingleLine else TextFieldLineLimits.MultiLine(maxHeightInLines = 5),
        textStyle = TextStyle.Default.copy(
            color = Gray90,
            fontSize = 16.sp,
        ),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        onKeyboardAction = { /*완료버튼을 눌렀을때 실행할 함수*/ },
        cursorBrush = Brush.linearGradient(
            colors = listOf(Gray40, Color.Transparent),
        ),
        outputTransformation = PasswordOutputTransformation(isPasswordType),
        decorator = { innerTextField ->
            Box(
                modifier = modifier
                    .clip(RoundedCornerShape(radius))
                    .background(Gray25)
                    .padding(vertical = 15.dp, horizontal = 20.dp),
            ) {
                if (textFieldState.text.isEmpty()) {
                    Text(
                        text = textHint,
                        textAlign = TextAlign.Center,
                        color = Gray40,
                    )
                }
                innerTextField()
            }
        },
    )
}

@Stable
private data class PasswordOutputTransformation(
    private val isPasswordType: Boolean,
) : OutputTransformation {
    override fun TextFieldBuffer.transformOutput() {
        if (isPasswordType) {
            for (index in 0 until length) {
                replace(index, index + 1, "*")
            }
        }
    }
}
