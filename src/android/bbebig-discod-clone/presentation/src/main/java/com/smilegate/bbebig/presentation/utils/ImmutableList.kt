package com.smilegate.bbebig.presentation.utils

import androidx.compose.runtime.Immutable

@Immutable
class ImmutableList<T>(private val list: List<T> = listOf()) : List<T> by list
