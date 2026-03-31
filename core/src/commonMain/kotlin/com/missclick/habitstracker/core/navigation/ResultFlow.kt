package com.missclick.habitstracker.core.navigation

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map

inline fun <reified T : Any> NavigationResults.resultFlow(key: String): Flow<T> {
    return results
        .filter { event -> event.key == key }
        .map { event ->
            event.value as? T
                ?: error(
                    "Navigation result for key='$key' is not a ${T::class.qualifiedName}. " +
                        "Actual=${event.value::class.qualifiedName}",
                )
        }
}
