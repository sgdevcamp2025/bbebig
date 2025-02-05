package com.smilegate.bbebig.presentation.utils

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material.ripple
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

fun Modifier.rippleSingleClick(
    delayMillis: Long = 1000L,
    onClick: () -> Unit,
): Modifier = composed {
    var isClickable by remember { mutableStateOf(true) }
    val coroutineScope = rememberCoroutineScope()

    clickable(
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

fun Modifier.noRippleSingleClick(
    delayMillis: Long = 1000L,
    onClick: () -> Unit,
): Modifier = composed {
    var isClickable by remember { mutableStateOf(true) }
    val coroutineScope = rememberCoroutineScope()

    clickable(
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

fun Modifier.rippleClick(
    onClick: () -> Unit,
): Modifier = composed {
    clickable(
        indication = ripple(bounded = false),
        interactionSource = remember { MutableInteractionSource() },
    ) {
        onClick()
    }
}

fun Modifier.noRippleClick(
    onClick: () -> Unit,
): Modifier = composed {
    clickable(
        indication = null,
        interactionSource = remember { MutableInteractionSource() },
    ) {
        onClick()
    }
}
