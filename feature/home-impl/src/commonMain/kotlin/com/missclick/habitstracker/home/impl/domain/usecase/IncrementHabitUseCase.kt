package com.missclick.habitstracker.home.impl.domain.usecase

import com.missclick.habitstracker.core.model.HabitId
import com.missclick.habitstracker.home.impl.domain.repository.IHomeRepository

internal class IncrementHabitUseCase(
    private val repository: IHomeRepository,
) {
    suspend operator fun invoke(habitId: HabitId) = repository.incrementHabit(habitId)
}
