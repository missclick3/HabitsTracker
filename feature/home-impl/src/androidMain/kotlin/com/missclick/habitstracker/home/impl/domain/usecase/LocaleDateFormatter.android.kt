package com.missclick.habitstracker.home.impl.domain.usecase

import kotlinx.datetime.LocalDate
import kotlinx.datetime.toJavaLocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

internal actual class LocaleDateFormatter {
    private val formatter = DateTimeFormatter.ofPattern(DATE_PATTERN, Locale.getDefault())

    actual fun format(date: LocalDate): String = date.toJavaLocalDate().format(formatter)

    private companion object {
        const val DATE_PATTERN = "EEEE, MMM d"
    }
}
