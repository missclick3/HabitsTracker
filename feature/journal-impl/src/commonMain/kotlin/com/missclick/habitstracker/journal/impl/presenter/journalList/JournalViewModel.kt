package com.missclick.habitstracker.journal.impl.presenter.journalList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.missclick.habitstracker.journal.impl.domain.model.JournalEntry
import com.missclick.habitstracker.journal.impl.domain.usecase.ObserveJournalUseCase
import com.missclick.habitstracker.journal.impl.domain.usecase.SeedJournalUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate

internal class JournalViewModel(
    private val observeJournal: ObserveJournalUseCase,
    private val seedJournal: SeedJournalUseCase,
) : ViewModel() {

    private val mutableState = MutableStateFlow(JournalState.default())
    val state: StateFlow<JournalState> = mutableState.asStateFlow()

    fun onIntent(intent: JournalIntent) {
        when (intent) {
            JournalIntent.Load -> load()
        }
    }

    private fun load() {
        viewModelScope.launch { seedJournal() }
        observeJournal()
            .onEach { entries ->
                mutableState.value = JournalState(
                    entries = entries.map { it.toItem() },
                    isLoading = false,
                )
            }
            .launchIn(viewModelScope)
    }

    private fun JournalEntry.toItem() = JournalEntryItem(
        dateLabel = date.toLabel(),
        mood = mood,
        note = note,
    )

    private fun LocalDate.toLabel(): String {
        val dow = dayOfWeek.name.take(3).lowercase().replaceFirstChar { it.uppercase() }
        val mon = month.name.take(3).lowercase().replaceFirstChar { it.uppercase() }
        return "$dow, $mon $dayOfMonth"
    }
}