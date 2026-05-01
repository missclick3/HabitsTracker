package com.missclick.habitstracker.core.navigation

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey

typealias FeatureEntryBuilder = EntryProviderScope<NavKey>.() -> Unit

interface Navigator {
    val backStack: SnapshotStateList<NavKey>

    fun navigate(key: NavKey)

    fun back(): Boolean

    fun popUpTo(predicate: (NavKey) -> Boolean)

    fun replaceAll(key: NavKey)
}

fun createNavigator(start: NavKey): Navigator = CoreNavigator(start)

internal class CoreNavigator(start: NavKey) : Navigator {
    override val backStack: SnapshotStateList<NavKey> = mutableStateListOf(start)

    override fun navigate(key: NavKey) {
        backStack.add(key)
    }

    override fun back(): Boolean = backStack.removeLastOrNull() != null

    override fun popUpTo(predicate: (NavKey) -> Boolean) {
        while (backStack.size > 1 && !predicate(backStack.last())) backStack.removeLast()
    }

    override fun replaceAll(key: NavKey) {
        backStack.clear()
        backStack.add(key)
    }
}
