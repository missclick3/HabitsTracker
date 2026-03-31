package com.missclick.habitstracker.core.navigation

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class NavigationResultStore : NavigationResults {
    private val mutableResults = MutableSharedFlow<NavigationResults.ResultEvent>(
        replay = 1,
        extraBufferCapacity = 64,
    )

    override val results = mutableResults.asSharedFlow()

    override fun set(key: String, value: Any) {
        mutableResults.tryEmit(
            NavigationResults.ResultEvent(
                key = key,
                value = value,
            ),
        )
    }

    override fun clear(key: String) {
        mutableResults.tryEmit(
            NavigationResults.ResultEvent(
                key = key,
                value = Unit,
            ),
        )
    }
}
