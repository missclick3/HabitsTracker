package com.missclick.habitstracker.journal.impl.domain.model

import com.missclick.habitstracker.core.model.Mood
import kotlinx.datetime.LocalDate

internal data class JournalEntry(
    val date: LocalDate,
    val mood: Mood?,
    val note: String,
)