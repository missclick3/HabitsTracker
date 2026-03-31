package com.missclick.habitstracker.core.navigation

interface AppComposeNavigator {
    fun navigate(destination: AppScreen)

    fun navigateAndClearBackStack(destination: AppScreen)

    fun popUpTo(destination: AppScreen, inclusive: Boolean = false)

    fun <T : Any> navigateBackWithResult(
        key: String,
        result: T,
        destination: AppScreen? = null,
    )

    fun navigateUp()

    suspend fun bind(
        backStack: MutableList<AppScreen>,
        results: NavigationResults,
    )
}
