package com.missclick.habitstracker.home.impl.domain.model

import com.missclick.habitstracker.core.model.HabitId
import com.missclick.habitstracker.core.model.HabitKind

internal data class Habit(
    val id: HabitId,
    val title: String,
    val kind: HabitKind,
    val targetCount: Int?,
)
