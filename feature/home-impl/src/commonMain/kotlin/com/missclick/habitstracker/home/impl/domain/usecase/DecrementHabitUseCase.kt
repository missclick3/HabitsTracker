package com.missclick.habitstracker.home.impl.domain.usecase

import com.missclick.habitstracker.core.model.HabitId
import com.missclick.habitstracker.home.impl.domain.repository.IHomeRepository

internal class DecrementHabitUseCase(
    private val repository: IHomeRepository,
) {
    suspend operator fun invoke(habitId: HabitId) = repository.decrementHabit(habitId)
}
