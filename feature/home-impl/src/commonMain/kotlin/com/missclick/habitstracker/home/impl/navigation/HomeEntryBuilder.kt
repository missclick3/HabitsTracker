package com.missclick.habitstracker.home.impl.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.missclick.habitstracker.home.api.HomeScreenRoute
import com.missclick.habitstracker.home.impl.presenter.editHabit.EditHabitNavigation
import com.missclick.habitstracker.home.impl.presenter.mainScreen.HomeNavigation

fun EntryProviderScope<NavKey>.homeEntries() {
    entry<HomeScreenRoute.HomeScreen> { _ ->
        HomeNavigation()
    }
    entry<HomeScreenRoute.EditHabitScreen> { route ->
        EditHabitNavigation(habitId = route.habitId)
    }
}
