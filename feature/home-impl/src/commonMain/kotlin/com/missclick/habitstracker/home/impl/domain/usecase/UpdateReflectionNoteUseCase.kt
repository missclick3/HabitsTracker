package com.missclick.habitstracker.home.impl.domain.usecase

import com.missclick.habitstracker.home.impl.domain.repository.IHomeRepository

internal class UpdateReflectionNoteUseCase(
    private val repository: IHomeRepository,
) {
    suspend operator fun invoke(note: String) = repository.updateReflectionNote(note)
}
