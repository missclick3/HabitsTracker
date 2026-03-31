package com.missclick.habitstracker.core.navigation

internal sealed interface NavCommand {
    data object NavigateUp : NavCommand

    data class NavigateTo(val destination: AppScreen) : NavCommand

    data class NavigateAndClear(val destination: AppScreen) : NavCommand

    data class PopUpTo(val destination: AppScreen, val inclusive: Boolean) : NavCommand

    data class NavigateUpWithResult(
        val key: String,
        val value: Any,
        val destination: AppScreen?,
    ) : NavCommand
}
