package com.missclick.habitstracker.home.impl.domain.usecase

import kotlinx.datetime.LocalDate
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

internal actual class LocaleDateFormatter {
    actual fun format(date: LocalDate): String {
        val calendar =
            Calendar.getInstance().apply {
                clear()
                set(date.year, date.month.ordinal, date.day)
            }

        val formatter = SimpleDateFormat(DATE_PATTERN, Locale.getDefault())
        return formatter.format(calendar.time)
    }

    private companion object {
        const val DATE_PATTERN = "EEEE, MMM d"
    }
}
