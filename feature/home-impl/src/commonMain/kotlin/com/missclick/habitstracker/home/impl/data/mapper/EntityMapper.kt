package com.missclick.habitstracker.home.impl.data.mapper

import com.missclick.habitstracker.core.model.HabitId
import com.missclick.habitstracker.core.model.HabitKind
import com.missclick.habitstracker.database.entity.HabitDailyRecordEntity
import com.missclick.habitstracker.database.entity.HabitEntity
import com.missclick.habitstracker.home.impl.domain.model.Habit
import com.missclick.habitstracker.home.impl.domain.repository.HomeHabit

internal fun HabitEntity.toHomeHabit(record: HabitDailyRecordEntity?): HomeHabit =
    HomeHabit(
        id = HabitId(id),
        title = title,
        kind = HabitKind.valueOf(kind),
        isCompleted = record?.isCompleted ?: false,
        currentCount = record?.currentCount ?: 0,
        targetCount = targetCount,
    )

internal fun Habit.toEntity(): HabitEntity =
    HabitEntity(
        id = id.value,
        title = title,
        kind = kind.name,
        targetCount = targetCount,
    )

internal fun HabitEntity.toDomain(): Habit =
    Habit(
        id = HabitId(id),
        title = title,
        kind = HabitKind.valueOf(kind),
        targetCount = targetCount,
    )

internal fun defaultHabits(): List<HabitEntity> = listOf(
    HabitEntity(id = "hydration", title = "Hydration", kind = "Count", targetCount = 8),
    HabitEntity(id = "mindfulness", title = "Mindfulness", kind = "Binary", targetCount = null),
)