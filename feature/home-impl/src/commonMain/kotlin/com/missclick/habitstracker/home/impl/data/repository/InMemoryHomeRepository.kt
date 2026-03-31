package com.missclick.habitstracker.home.impl.data.repository

import com.missclick.habitstracker.core.model.HabitId
import com.missclick.habitstracker.core.model.HabitKind
import com.missclick.habitstracker.core.model.Mood
import com.missclick.habitstracker.home.impl.domain.repository.HomeHabit
import com.missclick.habitstracker.home.impl.domain.repository.HomeReflection
import com.missclick.habitstracker.home.impl.domain.repository.IHomeRepository
import com.missclick.habitstracker.home.impl.domain.repository.HomeSnapshot
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

internal class InMemoryHomeRepository : IHomeRepository {
    private val snapshot = MutableStateFlow(
        HomeSnapshot(
            greetingLabel = null,
            dateLabel = "",
            habits = listOf(
                HomeHabit(
                    id = HabitId("hydration"),
                    title = "Hydration",
                    kind = HabitKind.Count,
                    isCompleted = false,
                    currentCount = 6,
                    targetCount = 8,
                ),
                HomeHabit(
                    id = HabitId("mindfulness"),
                    title = "Mindfulness",
                    kind = HabitKind.Binary,
                    isCompleted = true,
                    currentCount = 0,
                    targetCount = null,
                ),
            ),
            reflection = HomeReflection(
                selectedMood = Mood.Good,
                note = "Felt focused after a quiet morning walk.",
            ),
        ),
    )

    override fun observeHome(): Flow<HomeSnapshot> = snapshot.asStateFlow()

    override suspend fun toggleHabit(habitId: HabitId) {
        snapshot.update { current ->
            current.copy(
                habits = current.habits.map { habit ->
                    if (habit.id != habitId || habit.kind != HabitKind.Binary) {
                        habit
                    } else {
                        val nextCompleted = !habit.isCompleted
                        habit.copy(
                            isCompleted = nextCompleted,
                        )
                    }
                },
            )
        }
    }

    override suspend fun incrementHabit(habitId: HabitId) {
        snapshot.update { current ->
            current.copy(
                habits = current.habits.map { habit ->
                    if (habit.id != habitId || habit.kind != HabitKind.Count) {
                        habit
                    } else {
                        val target = habit.targetCount ?: return@map habit
                        val nextCount = (habit.currentCount + 1).coerceAtMost(target)
                        habit.copy(
                            currentCount = nextCount,
                            isCompleted = nextCount >= target,
                        )
                    }
                },
            )
        }
    }

    override suspend fun decrementHabit(habitId: HabitId) {
        snapshot.update { current ->
            current.copy(
                habits = current.habits.map { habit ->
                    if (habit.id != habitId || habit.kind != HabitKind.Count) {
                        habit
                    } else {
                        val target = habit.targetCount ?: return@map habit
                        val nextCount = (habit.currentCount - 1).coerceAtLeast(0)
                        habit.copy(
                            currentCount = nextCount,
                            isCompleted = nextCount >= target,
                        )
                    }
                },
            )
        }
    }

    override suspend fun updateMood(mood: Mood) {
        snapshot.update { current ->
            current.copy(reflection = current.reflection.copy(selectedMood = mood))
        }
    }

    override suspend fun updateReflectionNote(note: String) {
        snapshot.update { current ->
            current.copy(reflection = current.reflection.copy(note = note))
        }
    }
}