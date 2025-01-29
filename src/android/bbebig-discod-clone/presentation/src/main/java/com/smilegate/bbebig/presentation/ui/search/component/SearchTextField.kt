package com.smilegate.bbebig.presentation.ui.search.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smilegate.bbebig.presentation.theme.Gray25
import com.smilegate.bbebig.presentation.theme.Gray40
import com.smilegate.bbebig.presentation.theme.Gray90
import com.smilegate.bbebig.presentation.utils.StableImage
import com.smilegate.devcamp.presentation.R

@Composable
fun SearchTextField(
    modifier: Modifier,
    textFieldState: TextFieldState,
    textHint: String = "",
    keyboardType: KeyboardType = KeyboardType.Text,
) {
    BasicTextField(
        modifier = modifier,
        state = textFieldState,
        lineLimits = TextFieldLineLimits.SingleLine,
        textStyle = TextStyle.Default.copy(
            color = Gray90,
            fontSize = 16.sp,
        ),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        onKeyboardAction = { /*완료버튼을 눌렀을때 실행할 함수*/ },
        cursorBrush = Brush.linearGradient(
            colors = listOf(Gray40, Color.Transparent),
        ),
        decorator = { innerTextField ->
            Row(
                modifier = modifier
                    .clip(RoundedCornerShape(30.dp))
                    .background(Gray25)
                    .padding(vertical = 10.dp, horizontal = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                StableImage(
                    modifier = Modifier
                        .padding(end = 20.dp)
                        .size(20.dp)
                        .background(Color.Transparent),
                    drawableResId = R.drawable.ic_search,
                )
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

@Composable
@Preview
private fun SearchTextFieldPreview() {
    SearchTextField(
        modifier = Modifier.fillMaxWidth(),
        textFieldState = TextFieldState(),
        textHint = "검색어를 입력해주세요",
    )
}
