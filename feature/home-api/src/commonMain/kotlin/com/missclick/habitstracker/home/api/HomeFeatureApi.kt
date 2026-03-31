package com.missclick.habitstracker.home.api

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.missclick.habitstracker.core.model.HabitId
import com.missclick.habitstracker.core.navigation.AppScreen
import kotlinx.serialization.Serializable

interface HomeFeatureApi {
    val route: HomeRoute

    @Composable
    fun Screen(
        modifier: Modifier = Modifier,
        callbacks: HomeCallbacks,
    )
}

@Serializable
data object HomeRoute : AppScreen

data class HomeCallbacks(
    val onOpenArchive: () -> Unit,
    val onOpenCreateHabit: () -> Unit,
    val onOpenEditHabit: (HabitId) -> Unit,
)
