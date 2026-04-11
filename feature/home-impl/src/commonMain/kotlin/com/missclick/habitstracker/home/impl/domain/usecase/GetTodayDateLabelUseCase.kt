package com.missclick.habitstracker.home.impl.domain.usecase

import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import kotlin.time.Clock

internal class GetTodayDateLabelUseCase(
    private val dateProvider: DateProvider = SystemDateProvider(),
    private val dateFormatter: LocaleDateFormatter = LocaleDateFormatter(),
) {
    operator fun invoke(): String = dateFormatter.format(dateProvider.today())
}

internal fun interface DateProvider {
    fun today(): LocalDate
}

internal class SystemDateProvider : DateProvider {
    override fun today(): LocalDate = Clock.System.todayIn(TimeZone.currentSystemDefault())
}
