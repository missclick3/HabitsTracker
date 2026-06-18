package com.missclick.habitstracker.home.impl.data.network.dto

import kotlinx.serialization.Serializable

@Serializable
internal data class QuoteOfDayResponse(
    val quote: String,
    val author: String,
)