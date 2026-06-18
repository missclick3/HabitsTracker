package com.missclick.habitstracker.journal.impl.presenter.journalList

import com.missclick.habitstracker.core.model.Mood

internal sealed interface JournalIntent {
    data object Load : JournalIntent
}

internal data class JournalState(
    val entries: List<JournalEntryItem>,
    val isLoading: Boolean,
) {
    companion object {
        fun default() = JournalState(entries = emptyList(), isLoading = true)
    }
}

internal data class JournalEntryItem(
    val dateLabel: String,
    val mood: Mood?,
    val note: String,
)