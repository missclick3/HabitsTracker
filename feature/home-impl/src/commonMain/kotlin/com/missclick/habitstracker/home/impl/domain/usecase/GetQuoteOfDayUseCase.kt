package com.missclick.habitstracker.home.impl.domain.usecase

import com.missclick.habitstracker.home.impl.data.repository.QuoteRepository
import com.missclick.habitstracker.home.impl.domain.model.Quote

internal class GetQuoteOfDayUseCase(
    private val repo: QuoteRepository,
    private val dateProvider: DateProvider,
) {
    suspend operator fun invoke(): Quote? = repo.getQuoteOfDay(dateProvider.today().toString())
}