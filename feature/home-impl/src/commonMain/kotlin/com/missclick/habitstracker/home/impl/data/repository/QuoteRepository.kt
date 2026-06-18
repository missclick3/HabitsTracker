package com.missclick.habitstracker.home.impl.data.repository

import com.missclick.habitstracker.database.dao.QuoteDao
import com.missclick.habitstracker.database.entity.QuoteEntity
import com.missclick.habitstracker.home.impl.data.network.QuoteApiDataSource
import com.missclick.habitstracker.home.impl.domain.model.Quote

internal class QuoteRepository(
    private val dao: QuoteDao,
    private val dataSource: QuoteApiDataSource,
) {
    suspend fun getQuoteOfDay(today: String): Quote? {
        dao.getByDate(today)?.let { return Quote(it.text, it.author) }
        return runCatching {
            val dto = dataSource.fetchQuoteOfDay()
            dao.upsert(QuoteEntity(today, dto.quote, dto.author))
            Quote(dto.quote, dto.author)
        }.onFailure { e ->
            println("QuoteRepository error: ${e::class.simpleName}: ${e.message}")
        }.getOrNull()
    }
}