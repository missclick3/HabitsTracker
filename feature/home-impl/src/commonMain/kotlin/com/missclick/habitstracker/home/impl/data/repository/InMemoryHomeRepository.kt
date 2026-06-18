package com.missclick.habitstracker.home.impl.data.repository

import com.missclick.habitstracker.core.model.HabitId
import com.missclick.habitstracker.core.model.HabitKind
import com.missclick.habitstracker.core.model.Mood
import com.missclick.habitstracker.home.impl.domain.model.Habit
import com.missclick.habitstracker.home.impl.domain.repository.HomeHabit
import com.missclick.habitstracker.home.impl.domain.repository.HomeReflection
import com.missclick.habitstracker.home.impl.domain.repository.HomeSnapshot
import com.missclick.habitstracker.home.impl.domain.repository.IHomeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

internal class InMemoryHomeRepository : IHomeRepository {
    private val snapshot =
        MutableStateFlow(
            HomeSnapshot(
                dateLabel = "",
                userName = "You",
                habits =
                    listOf(
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
                reflection =
                    HomeReflection(
                        selectedMood = Mood.Good,
                        note = "",
                    ),
            ),
        )

    override fun observeHome(): Flow<HomeSnapshot> = snapshot.asStateFlow()

    override suspend fun toggleHabit(habitId: HabitId) {
        snapshot.update { current ->
            current.copy(
                habits =
                    current.habits.map { habit ->
                        if (habit.id != habitId || habit.kind != HabitKind.Binary) {
                            habit
                        } else {
                            habit.copy(isCompleted = !habit.isCompleted)
                        }
                    },
            )
        }
    }

    override suspend fun incrementHabit(habitId: HabitId) {
        snapshot.update { current ->
            current.copy(
                habits =
                    current.habits.map { habit ->
                        if (habit.id != habitId || habit.kind != HabitKind.Count) {
                            habit
                        } else {
                            val target = habit.targetCount ?: return@map habit
                            val nextCount = (habit.currentCount + 1).coerceAtMost(target)
                            habit.copy(currentCount = nextCount, isCompleted = nextCount >= target)
                        }
                    },
            )
        }
    }

    override suspend fun decrementHabit(habitId: HabitId) {
        snapshot.update { current ->
            current.copy(
                habits =
                    current.habits.map { habit ->
                        if (habit.id != habitId || habit.kind != HabitKind.Count) {
                            habit
                        } else {
                            val target = habit.targetCount ?: return@map habit
                            val nextCount = (habit.currentCount - 1).coerceAtLeast(0)
                            habit.copy(currentCount = nextCount, isCompleted = nextCount >= target)
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

    override suspend fun createHabit(habit: Habit) {
        snapshot.update { current ->
            current.copy(
                habits = current.habits + HomeHabit(
                    id = habit.id,
                    title = habit.title,
                    kind = habit.kind,
                    isCompleted = false,
                    currentCount = 0,
                    targetCount = habit.targetCount,
                ),
            )
        }
    }

    override suspend fun updateHabit(habit: Habit) {
        snapshot.update { current ->
            current.copy(
                habits = current.habits.map { h ->
                    if (h.id == habit.id) {
                        h.copy(title = habit.title, kind = habit.kind, targetCount = habit.targetCount)
                    } else {
                        h
                    }
                },
            )
        }
    }

    override suspend fun deleteHabit(habitId: HabitId) {
        snapshot.update { current ->
            current.copy(habits = current.habits.filter { it.id != habitId })
        }
    }

    override suspend fun getHabitById(id: HabitId): Habit? =
        snapshot.value.habits.find { it.id == id }?.let { h ->
            Habit(id = h.id, title = h.title, kind = h.kind, targetCount = h.targetCount)
        }
}