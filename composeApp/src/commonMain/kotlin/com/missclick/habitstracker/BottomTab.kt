package com.missclick.habitstracker

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation3.runtime.NavKey
import com.missclick.habitstracker.home.api.HomeScreenRoute

enum class BottomTab(
    val icon: ImageVector,
) {
    HOME(Icons.Default.Home),
    JOURNAL(Icons.Default.List),
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
