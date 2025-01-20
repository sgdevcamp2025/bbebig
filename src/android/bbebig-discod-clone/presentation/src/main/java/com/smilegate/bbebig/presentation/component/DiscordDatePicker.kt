package com.smilegate.bbebig.presentation.component

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smilegate.bbebig.presentation.theme.Gray20
import com.smilegate.bbebig.presentation.theme.Gray50
import com.smilegate.bbebig.presentation.theme.Gray90
import com.smilegate.bbebig.presentation.utils.rippleSingleClick
import com.smilegate.devcamp.presentation.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiscordDatePicker(
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit,
) {
    val datePickerState = rememberDatePickerState(
        initialDisplayMode = DisplayMode.Picker,
        initialSelectedDateMillis = System.currentTimeMillis(),
        selectableDates = DatePickerDefaults.AllDates,
    )

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Text(
                modifier = Modifier
                    .padding(10.dp)
                    .rippleSingleClick {
                        onDateSelected(datePickerState.selectedDateMillis)
                        onDismiss()
                    },
                text = "Confirm",
                color = Gray90,
            )
        },
        colors = DatePickerDefaults.colors(
            containerColor = Gray20,
            headlineContentColor = Gray90,
        ),
        dismissButton = {
            Text(
                modifier = Modifier
                    .padding(10.dp)
                    .rippleSingleClick { onDismiss() },
                text = "Cancel",
                color = Gray90,
            )
        },
    ) {
        DatePicker(
            state = datePickerState,
            title = {
                Text(
                    modifier = Modifier.padding(14.dp),
                    fontSize = 20.sp,
                    text = stringResource(R.string.birth_sub_title),
                    color = Gray90,
                )
            },
            showModeToggle = false,
            colors = DatePickerDefaults.colors(
                containerColor = Gray20,
                headlineContentColor = Gray90,
                selectedDayContainerColor = Gray50,
                selectedDayContentColor = Gray90,
                navigationContentColor = Gray50,
                yearContentColor = Gray50,
                currentYearContentColor = Gray90,
                selectedYearContainerColor = Gray50,
                selectedYearContentColor = Gray90,
                todayDateBorderColor = Gray90,
                todayContentColor = Gray90,
                dayContentColor = Gray50,
                dividerColor = Color.Transparent,
            ),
        )
    }
}
