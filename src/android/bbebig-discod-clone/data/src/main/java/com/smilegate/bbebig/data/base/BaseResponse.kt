package com.smilegate.bbebig.data.base

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BaseResponse<T>(
    @SerialName("code")
    val code: String,
    @SerialName("message")
    val message: String,
    @SerialName("result")
    val result: T?,
)

suspend fun <T> callApi(
    execute: suspend () -> BaseResponse<T>,
): T {
    return runCatching {
        execute().result ?: throw NoSuchElementException()
    }.getOrElse { e ->
        throw Exception(e)
    }
}
