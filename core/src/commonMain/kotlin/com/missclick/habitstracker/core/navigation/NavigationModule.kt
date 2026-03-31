package com.missclick.habitstracker.core.navigation

import org.koin.core.module.Module
import org.koin.dsl.module

val navigationModule: Module = module {
    single<AppComposeNavigator> { ComposeNavigator() }
    single<NavigationResults> { NavigationResultStore() }
}
