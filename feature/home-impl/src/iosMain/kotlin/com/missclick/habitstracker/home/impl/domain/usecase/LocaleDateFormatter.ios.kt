package com.missclick.habitstracker.home.impl.domain.usecase

import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toNSDate
import platform.Foundation.NSDateFormatter

internal actual class LocaleDateFormatter {
    private val formatter = NSDateFormatter().apply {
        dateFormat = DATE_PATTERN
    }

    actual fun format(date: LocalDate): String {
        val startOfDay = date.atStartOfDayIn(TimeZone.currentSystemDefault())
        return formatter.stringFromDate(startOfDay.toNSDate())
    }

    private companion object {
        const val DATE_PATTERN = "EEEE, MMM d"
    }
}
