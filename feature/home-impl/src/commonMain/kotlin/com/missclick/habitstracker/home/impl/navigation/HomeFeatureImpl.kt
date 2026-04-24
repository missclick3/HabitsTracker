package com.missclick.habitstracker.home.impl.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.missclick.habitstracker.home.api.HomeCallbacks
import com.missclick.habitstracker.home.api.HomeFeatureApi
import com.missclick.habitstracker.home.api.HomeRoute
import com.missclick.habitstracker.home.impl.presenter.HomeEffect
import com.missclick.habitstracker.home.impl.presenter.HomeIntent
import com.missclick.habitstracker.home.impl.presenter.HomeViewModel
import com.missclick.habitstracker.home.impl.ui.HomeScreen
import org.koin.compose.viewmodel.koinViewModel

internal class HomeFeatureImpl : HomeFeatureApi {
    override val route = HomeRoute

    @Composable
    override fun Screen(
        modifier: Modifier,
        callbacks: HomeCallbacks,
    ) {
        val viewModel = koinViewModel<HomeViewModel>()
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

        HomeScreen(
            modifier = modifier,
            state = state,
            onIntent = viewModel::onIntent,
        )
    }
}
