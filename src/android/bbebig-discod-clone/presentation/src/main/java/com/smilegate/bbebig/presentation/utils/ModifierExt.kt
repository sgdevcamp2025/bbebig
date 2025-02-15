package com.smilegate.bbebig.presentation.utils

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun Modifier.rippleSingleClick(
    delayMillis: Long = 1000L,
    onClick: () -> Unit,
): Modifier {
    var isClickable by remember { mutableStateOf(true) }
    val coroutineScope = rememberCoroutineScope()

    return this then Modifier.clickable(
        indication = ripple(bounded = false),
        interactionSource = remember { MutableInteractionSource() },
    ) {
        if (isClickable) {
            onClick()
            isClickable = false
            coroutineScope.launch {
                delay(delayMillis)
                isClickable = true
            }
        }
    }
}

@Composable
fun Modifier.noRippleSingleClick(
    delayMillis: Long = 1000L,
    onClick: () -> Unit,
): Modifier {
    var isClickable by remember { mutableStateOf(true) }
    val coroutineScope = rememberCoroutineScope()

    return this then Modifier.clickable(
        indication = null,
        interactionSource = remember { MutableInteractionSource() },
        enabled = isClickable,
    ) {
        if (isClickable) {
            onClick()
            isClickable = false
            coroutineScope.launch {
                delay(delayMillis)
                isClickable = true
            }
        }
    }
}

@Composable
fun Modifier.rippleClick(
    onClick: () -> Unit,
): Modifier {
    return this then Modifier.clickable(
        indication = ripple(bounded = false),
        interactionSource = remember { MutableInteractionSource() },
    ) {
        onClick()
    }
}

@Composable
fun Modifier.noRippleClick(
    onClick: () -> Unit,
): Modifier {
    return this then Modifier.clickable(
        indication = null,
        interactionSource = remember { MutableInteractionSource() },
    ) {
        onClick()
    }
}
