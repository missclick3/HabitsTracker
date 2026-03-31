package com.missclick.habitstracker.home.impl.domain.usecase

import com.missclick.habitstracker.core.model.Mood
import com.missclick.habitstracker.home.impl.domain.repository.IHomeRepository

internal class UpdateReflectionMoodUseCase(
    private val repository: IHomeRepository,
) {
    suspend operator fun invoke(mood: Mood) = repository.updateMood(mood)
}
