package com.missclick.habitstracker.home.impl.domain.usecase

import kotlinx.datetime.LocalDate

internal expect class LocaleDateFormatter() {
    fun format(date: LocalDate): String
}
