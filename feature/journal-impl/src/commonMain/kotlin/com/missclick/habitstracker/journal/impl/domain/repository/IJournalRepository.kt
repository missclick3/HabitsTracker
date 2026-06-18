package com.missclick.habitstracker.journal.impl.domain.repository

import com.missclick.habitstracker.journal.impl.domain.model.JournalEntry
import kotlinx.coroutines.flow.Flow

internal interface IJournalRepository {
    fun observeJournal(): Flow<List<JournalEntry>>
}