package com.missclick.habitstracker.home.impl.data.network

import com.missclick.habitstracker.home.impl.data.network.dto.QuoteOfDayResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header

internal class QuoteApiDataSource(
    private val client: HttpClient,
    private val apiKey: String,
) {
    suspend fun fetchQuoteOfDay(): QuoteOfDayResponse =
        client.get("https://api.api-ninjas.com/v2/quoteoftheday") {
            header("X-Api-Key", apiKey)
        }.body<List<QuoteOfDayResponse>>().first()
}