package com.missclick.habitstracker.journal.api

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface JournalScreenRoute : NavKey {
    @Serializable data object JournalScreen : JournalScreenRoute
}