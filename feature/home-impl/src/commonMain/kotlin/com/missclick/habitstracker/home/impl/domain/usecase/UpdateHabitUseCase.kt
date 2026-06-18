package com.missclick.habitstracker.home.impl.domain.usecase

import com.missclick.habitstracker.home.impl.domain.model.Habit
import com.missclick.habitstracker.home.impl.domain.repository.IHomeRepository

internal class UpdateHabitUseCase(private val repository: IHomeRepository) {
    suspend operator fun invoke(habit: Habit) = repository.updateHabit(habit)
}