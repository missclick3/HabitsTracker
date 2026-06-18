package com.missclick.habitstracker.home.impl.presenter.editHabit

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.missclick.habitstracker.home.impl.ui.EditHabitScreen
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
internal fun EditHabitNavigation(habitId: String?) {
    val viewModel = koinViewModel<EditHabitViewModel> { parametersOf(habitId) }
    val state by viewModel.state.collectAsState()

    EditHabitScreen(
        state = state,
        onIntent = viewModel::onIntent,
    )
}