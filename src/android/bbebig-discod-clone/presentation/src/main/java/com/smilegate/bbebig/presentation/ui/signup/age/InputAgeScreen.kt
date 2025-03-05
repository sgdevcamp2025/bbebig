package com.smilegate.bbebig.presentation.ui.signup.age

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smilegate.bbebig.presentation.component.DiscordDatePicker
import com.smilegate.bbebig.presentation.component.DiscordRoundButton
import com.smilegate.bbebig.presentation.component.DiscordTopBar
import com.smilegate.bbebig.presentation.theme.Blue70
import com.smilegate.bbebig.presentation.theme.Gray20
import com.smilegate.bbebig.presentation.theme.Gray25
import com.smilegate.bbebig.presentation.theme.Gray90
import com.smilegate.bbebig.presentation.theme.White
import com.smilegate.bbebig.presentation.utils.LocalDateUtil
import com.smilegate.bbebig.presentation.utils.rippleSingleClick
import com.smilegate.devcamp.presentation.R
import java.util.Calendar

@Composable
fun AgeScreen(
    onBackClick: () -> Unit,
    onClickConfirm: (String) -> Unit,
) {
    var isDatePickerDialogShow by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf(LocalDateUtil.getCurrentDate()) }

    Column(
        modifier = Modifier
            .background(Gray20)
            .fillMaxSize()
            .navigationBarsPadding()
            .padding(horizontal = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        DiscordTopBar(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding(),
            onBackClick = onBackClick,
            backButtonColor = White,
        )
        Text(
            modifier = Modifier,
            text = stringResource(R.string.age_title),
            fontSize = 24.sp,
            textAlign = TextAlign.Center,
            color = Gray90,
        )
        DateContainer(
            modifier = Modifier,
            selectedDate = selectedDate,
            onDateAreaClick = { isDatePickerDialogShow = true },
        )
        DiscordRoundButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 15.dp),
            textResId = R.string.finish_sign_up,
            backgroundColor = Blue70,
            onClick = {
                onClickConfirm(selectedDate)
            },
            textColor = White,
            radiusDp = 5.dp,
        )
    }

    if (isDatePickerDialogShow) {
        DiscordDatePicker(
            onDismiss = { isDatePickerDialogShow = false },
            onDateSelected = {
                if (it != null) {
                    val calendar = Calendar.getInstance()
                    calendar.timeInMillis = it
                    selectedDate = LocalDateUtil.getCalenderDate(calendar)
                }
            },
        )
    }
}

@Composable
private fun DateContainer(modifier: Modifier, selectedDate: String, onDateAreaClick: () -> Unit) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            modifier = Modifier.padding(top = 20.dp),
            text = stringResource(R.string.birth_sub_title),
            color = Gray90,
        )
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .rippleSingleClick { onDateAreaClick() }
                .clip(RoundedCornerShape(8.dp))
                .background(Gray25)
                .padding(vertical = 15.dp, horizontal = 20.dp),
            text = selectedDate,
            color = Gray90,
        )
    }
}

@Composable
@Preview
private fun AgeScreenPreview() {
    AgeScreen(
        onBackClick = { },
        onClickConfirm = { },
    )
}
