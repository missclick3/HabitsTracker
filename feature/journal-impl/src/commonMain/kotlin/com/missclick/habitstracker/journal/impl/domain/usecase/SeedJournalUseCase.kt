package com.missclick.habitstracker.journal.impl.domain.usecase

import com.missclick.habitstracker.journal.impl.data.repository.RoomJournalRepository

internal class SeedJournalUseCase(private val repo: RoomJournalRepository) {
    suspend operator fun invoke() = repo.seedIfEmpty()
}