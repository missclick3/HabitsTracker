package com.missclick.habitstracker.core.navigation

import kotlinx.coroutines.flow.MutableSharedFlow

class ComposeNavigator : AppComposeNavigator {
    private val commands = MutableSharedFlow<NavCommand>(extraBufferCapacity = 64)

    override fun navigate(destination: AppScreen) {
        commands.tryEmit(NavCommand.NavigateTo(destination))
    }

    override fun navigateAndClearBackStack(destination: AppScreen) {
        commands.tryEmit(NavCommand.NavigateAndClear(destination))
    }

    override fun popUpTo(destination: AppScreen, inclusive: Boolean) {
        commands.tryEmit(NavCommand.PopUpTo(destination, inclusive))
    }

    override fun <T : Any> navigateBackWithResult(key: String, result: T, destination: AppScreen?) {
        commands.tryEmit(NavCommand.NavigateUpWithResult(key, result, destination))
    }

    override fun navigateUp() {
        commands.tryEmit(NavCommand.NavigateUp)
    }

    override suspend fun bind(
        backStack: MutableList<AppScreen>,
        results: NavigationResults,
    ) {
        commands.collect { command ->
            when (command) {
                NavCommand.NavigateUp -> {
                    if (backStack.size > 1) {
                        backStack.removeAt(backStack.lastIndex)
                    }
                }

                is NavCommand.NavigateTo -> backStack.add(command.destination)

                is NavCommand.NavigateAndClear -> {
                    backStack.clear()
                    backStack.add(command.destination)
                }

                is NavCommand.PopUpTo -> {
                    val index = backStack.indexOfLast { it == command.destination }
                    if (index == -1) {
                        return@collect
                    }

                    val targetSize = if (command.inclusive) index else index + 1
                    while (backStack.size > targetSize) {
                        backStack.removeAt(backStack.lastIndex)
                    }
                }

                is NavCommand.NavigateUpWithResult -> {
                    results.set(command.key, command.value)
                    command.destination?.let { target ->
                        val index = backStack.indexOfLast { it == target }
                        if (index != -1) {
                            while (backStack.size > index + 1) {
                                backStack.removeAt(backStack.lastIndex)
                            }
                            return@collect
                        }
                    }

                    if (backStack.size > 1) {
                        backStack.removeAt(backStack.lastIndex)
                    }
                }
            }
        }
    }
}
