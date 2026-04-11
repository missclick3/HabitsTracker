package com.missclick.habitstracker.core.navigation

import kotlinx.coroutines.flow.Flow

interface NavigationResults {
    val results: Flow<ResultEvent>

    data class ResultEvent(
        val key: String,
        val value: Any,
    )

    fun set(
        key: String,
        value: Any,
    )

    fun clear(key: String)
}
