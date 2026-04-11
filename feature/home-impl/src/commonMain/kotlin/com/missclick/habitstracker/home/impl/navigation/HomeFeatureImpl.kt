package com.missclick.habitstracker.home.impl.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.missclick.habitstracker.home.api.HomeCallbacks
import com.missclick.habitstracker.home.api.HomeFeatureApi
import com.missclick.habitstracker.home.api.HomeRoute
import com.missclick.habitstracker.home.impl.domain.mapper.HomeStateMapper
import com.missclick.habitstracker.home.impl.domain.repository.IHomeRepository
import com.missclick.habitstracker.home.impl.domain.usecase.DecrementHabitUseCase
import com.missclick.habitstracker.home.impl.domain.usecase.GetTodayDateLabelUseCase
import com.missclick.habitstracker.home.impl.domain.usecase.IncrementHabitUseCase
import com.missclick.habitstracker.home.impl.domain.usecase.ObserveHomeUseCase
import com.missclick.habitstracker.home.impl.domain.usecase.ToggleHabitUseCase
import com.missclick.habitstracker.home.impl.domain.usecase.UpdateReflectionMoodUseCase
import com.missclick.habitstracker.home.impl.domain.usecase.UpdateReflectionNoteUseCase
import com.missclick.habitstracker.home.impl.presenter.HomeEffect
import com.missclick.habitstracker.home.impl.presenter.HomeIntent
import com.missclick.habitstracker.home.impl.presenter.HomeViewModel
import com.missclick.habitstracker.home.impl.ui.HomeScreen

internal class HomeFeatureImpl(
    private val repository: IHomeRepository,
) : HomeFeatureApi {
    override val route = HomeRoute

    @Composable
    override fun Screen(
        modifier: Modifier,
        callbacks: HomeCallbacks,
    ) {
        val viewModel = remember(repository) {
            HomeViewModel(
                observeHome = ObserveHomeUseCase(repository, HomeStateMapper()),
                toggleHabit = ToggleHabitUseCase(repository),
                incrementHabit = IncrementHabitUseCase(repository),
                decrementHabit = DecrementHabitUseCase(repository),
                updateReflectionMood = UpdateReflectionMoodUseCase(repository),
                updateReflectionNote = UpdateReflectionNoteUseCase(repository),
                getTodayDateLabelUseCase = GetTodayDateLabelUseCase(),
            )
        }
        val state by viewModel.state.collectAsState()

        LaunchedEffect(viewModel) {
            viewModel.onIntent(HomeIntent.Load)
        }

        LaunchedEffect(viewModel, callbacks) {
            viewModel.effects.collect { effect ->
                when (effect) {
                    HomeEffect.OpenArchive -> callbacks.onOpenArchive()
                    HomeEffect.OpenCreateHabit -> callbacks.onOpenCreateHabit()
                    is HomeEffect.OpenEditHabit -> callbacks.onOpenEditHabit(effect.habitId)
                }
            }
        }

        DisposableEffect(viewModel) {
            onDispose(viewModel::clear)
        }

        HomeScreen(
            modifier = modifier,
            state = state,
            onIntent = viewModel::onIntent,
        )
    }
}
