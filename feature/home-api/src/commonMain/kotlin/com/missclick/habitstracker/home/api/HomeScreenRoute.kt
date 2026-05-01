package com.missclick.habitstracker.home.api

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface HomeScreenRoute : NavKey {
    @Serializable data object HomeScreen : HomeScreenRoute
}
