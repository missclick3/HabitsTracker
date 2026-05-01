package com.missclick.habitstracker

import androidx.navigation3.runtime.NavKey
import com.missclick.habitstracker.home.api.HomeScreenRoute

enum class BottomTab {
    HOME,
    JOURNAL,
}

fun NavKey.toBottomTabOrNull(): BottomTab? =
    when (this) {
        HomeScreenRoute.HomeScreen -> BottomTab.HOME
        JournalRoute -> BottomTab.JOURNAL
        else -> null
    }

fun BottomTab.toRoute(): NavKey =
    when (this) {
        BottomTab.HOME -> HomeScreenRoute.HomeScreen
        BottomTab.JOURNAL -> JournalRoute
    }
