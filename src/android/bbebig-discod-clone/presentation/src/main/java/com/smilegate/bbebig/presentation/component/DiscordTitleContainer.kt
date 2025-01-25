package com.smilegate.bbebig.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun DiscordTitleContainer(modifier: Modifier, firstTitleResId: Int, secondTitleResId: Int) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(5.dp),
    ) {
        Text(stringResource(firstTitleResId), textAlign = TextAlign.Center)
        Text(stringResource(secondTitleResId), textAlign = TextAlign.Center)
    }
}
