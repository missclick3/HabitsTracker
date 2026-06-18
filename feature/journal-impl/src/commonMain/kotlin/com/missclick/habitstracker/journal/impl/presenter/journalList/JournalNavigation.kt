package com.missclick.habitstracker.journal.impl.presenter.journalList

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.missclick.habitstracker.journal.impl.ui.JournalScreen
import org.koin.compose.viewmodel.koinViewModel

@Composable
internal fun JournalNavigation() {
    val viewModel = koinViewModel<JournalViewModel>()
    val state by viewModel.state.collectAsState()

    LaunchedEffect(viewModel) {
        viewModel.onIntent(JournalIntent.Load)
    }

    JournalScreen(state = state)
}