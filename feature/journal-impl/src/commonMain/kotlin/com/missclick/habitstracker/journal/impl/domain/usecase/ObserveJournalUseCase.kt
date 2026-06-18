package com.missclick.habitstracker.journal.impl.domain.usecase

import com.missclick.habitstracker.journal.impl.domain.model.JournalEntry
import com.missclick.habitstracker.journal.impl.domain.repository.IJournalRepository
import kotlinx.coroutines.flow.Flow

internal class ObserveJournalUseCase(private val repo: IJournalRepository) {
    operator fun invoke(): Flow<List<JournalEntry>> = repo.observeJournal()
}