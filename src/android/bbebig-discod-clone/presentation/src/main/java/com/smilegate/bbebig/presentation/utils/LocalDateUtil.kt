package com.smilegate.bbebig.presentation.utils

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

object LocalDateUtil {
    fun chatTime(date: String): String {
        date.split("T")
        val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val outputFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")
        return LocalDate.parse(date.split("T")[0], inputFormatter).format(outputFormatter)
    }

    fun getCurrentDate(): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.getDefault())
        return LocalDate.now().format(formatter)
    }

    fun getCalenderDate(calendar: Calendar): String {
        return String.format(
            Locale.getDefault(),
            "%04d-%02d-%02d",
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH) + 1,
            calendar.get(Calendar.DAY_OF_MONTH),
        )
    }
}
