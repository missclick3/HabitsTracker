package com.missclick.habitstracker

import com.missclick.habitstracker.core.navigation.AppScreen
import com.missclick.habitstracker.core.navigation.JournalRoute
import com.missclick.habitstracker.home.api.HomeRoute

enum class BottomTab(val root: AppScreen) {
    HOME(HomeRoute),
    JOURNAL(JournalRoute),
}

fun AppScreen.toBottomTabOrNull(): BottomTab? = when (this) {
    HomeRoute -> BottomTab.HOME
    JournalRoute -> BottomTab.JOURNAL
    else -> null
}
