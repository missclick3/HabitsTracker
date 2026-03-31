package com.missclick.habitstracker.core.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

interface AppScreen : NavKey

@Serializable
data object JournalRoute : AppScreen
