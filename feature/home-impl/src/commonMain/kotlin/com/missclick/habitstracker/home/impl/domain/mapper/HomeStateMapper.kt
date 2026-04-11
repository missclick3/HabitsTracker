package com.missclick.habitstracker.home.impl.domain.mapper

import com.missclick.habitstracker.core.model.HabitKind
import com.missclick.habitstracker.home.impl.domain.repository.HomeHabit
import com.missclick.habitstracker.home.impl.domain.repository.HomeSnapshot
import com.missclick.habitstracker.home.impl.presenter.HomeHabitItem
import com.missclick.habitstracker.home.impl.presenter.HomeHabitProgress
import com.missclick.habitstracker.home.impl.presenter.HomeState
import com.missclick.habitstracker.home.impl.presenter.ReflectionUiState

internal class HomeStateMapper {
    fun map(snapshot: HomeSnapshot): HomeState =
        HomeState(
            dateLabel = snapshot.dateLabel,
            habits = snapshot.habits.map(::mapHabit),
            reflection =
                ReflectionUiState(
                    selectedMood = snapshot.reflection.selectedMood,
                    note = snapshot.reflection.note,
                ),
            isEmpty = snapshot.habits.isEmpty(),
            isLoading = false,
        )

    private fun mapHabit(habit: HomeHabit): HomeHabitItem =
        HomeHabitItem(
            id = habit.id,
            title = habit.title,
            progress =
                when (habit.kind) {
                    HabitKind.Binary -> HomeHabitProgress.Binary(habit.isCompleted)
                    HabitKind.Count ->
                        HomeHabitProgress.Count(
                            current = habit.currentCount,
                            target = habit.targetCount ?: 0,
                            isCompleted = habit.isCompleted,
                        )
                },
        )
}
