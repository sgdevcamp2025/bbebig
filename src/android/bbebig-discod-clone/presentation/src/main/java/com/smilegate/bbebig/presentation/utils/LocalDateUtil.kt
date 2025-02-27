package com.smilegate.bbebig.presentation.utils

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.time.format.SignStyle
import java.time.temporal.ChronoField

object LocalDateUtil {
    fun format(date: String): String {
        val inputFormatter = DateTimeFormatterBuilder()
            .appendPattern("yyyy.")
            .appendValue(ChronoField.MONTH_OF_YEAR, 1, 2, SignStyle.NOT_NEGATIVE)
            .appendLiteral('.')
            .appendValue(ChronoField.DAY_OF_MONTH, 1, 2, SignStyle.NOT_NEGATIVE)
            .toFormatter()

        return LocalDate.parse(date, inputFormatter).format(DateTimeFormatter.ISO_DATE)
    }

    fun chatTime(date: String): String {
        date.split("T")
        val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val outputFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")
        return LocalDate.parse(date.split("T")[0], inputFormatter).format(outputFormatter)
    }
}
