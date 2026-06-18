package com.missclick.habitstracker.home.impl.presenter.mainScreen

import com.missclick.habitstracker.core.model.HabitId
import com.missclick.habitstracker.core.model.Mood
import com.missclick.habitstracker.core.utils.StringUtils.EMPTY_STRING

internal sealed interface HomeIntent {
    data object Load : HomeIntent

    data object ArchiveClicked : HomeIntent

    data object CreateHabitClicked : HomeIntent

    data class HabitClicked(val habitId: HabitId) : HomeIntent

    data class ToggleHabit(val habitId: HabitId) : HomeIntent

    data class IncrementHabit(val habitId: HabitId) : HomeIntent

    data class DecrementHabit(val habitId: HabitId) : HomeIntent

    data class MoodSelected(val mood: Mood) : HomeIntent

    data class ReflectionNoteChanged(val text: String) : HomeIntent
}

internal data class HomeState(
    val dateLabel: String,
    val userName: String,
    val habits: List<HomeHabitItem>,
    val reflection: ReflectionUiState,
    val isEmpty: Boolean,
    val isLoading: Boolean,
) {
    companion object {
        fun default() =
            HomeState(
                dateLabel = EMPTY_STRING,
                userName = EMPTY_STRING,
                habits = emptyList(),
                reflection = ReflectionUiState(),
                isEmpty = true,
                isLoading = true,
            )
    }
}

internal data class HomeHabitItem(
    val id: HabitId,
    val title: String,
    val progress: HomeHabitProgress,
)

internal sealed interface HomeHabitProgress {
    data class Binary(val isCompleted: Boolean) : HomeHabitProgress

    data class Count(
        val current: Int,
        val target: Int,
        val isCompleted: Boolean,
    ) : HomeHabitProgress
}

internal data class ReflectionUiState(
    val selectedMood: Mood? = null,
    val note: String = EMPTY_STRING,
)

internal sealed interface HomeEffect {
    data object OpenArchive : HomeEffect
}
