package com.missclick.habitstracker.home.impl.domain.repository

import com.missclick.habitstracker.core.model.HabitId
import com.missclick.habitstracker.core.model.HabitKind
import com.missclick.habitstracker.core.model.Mood
import kotlinx.coroutines.flow.Flow

internal interface IHomeRepository {
    fun observeHome(): Flow<HomeSnapshot>

    suspend fun toggleHabit(habitId: HabitId)

    suspend fun incrementHabit(habitId: HabitId)

    suspend fun decrementHabit(habitId: HabitId)

    suspend fun updateMood(mood: Mood)

    suspend fun updateReflectionNote(note: String)
}

internal data class HomeSnapshot(
    val greetingLabel: String?,
    val dateLabel: String,
    val habits: List<HomeHabit>,
    val reflection: HomeReflection,
)

internal data class HomeHabit(
    val id: HabitId,
    val title: String,
    val kind: HabitKind,
    val isCompleted: Boolean,
    val currentCount: Int,
    val targetCount: Int?,
)

internal data class HomeReflection(
    val selectedMood: Mood?,
    val note: String,
)
