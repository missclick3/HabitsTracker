package com.missclick.habitstracker.journal.impl.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.missclick.habitstracker.journal.api.JournalScreenRoute
import com.missclick.habitstracker.journal.impl.presenter.journalList.JournalNavigation

fun EntryProviderScope<NavKey>.journalEntries() {
    entry<JournalScreenRoute.JournalScreen> { _ ->
        JournalNavigation()
    }
}