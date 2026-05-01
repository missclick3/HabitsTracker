package com.missclick.habitstracker.home.impl.presenter.mainScreen

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.missclick.habitstracker.home.impl.ui.HomeScreen
import org.koin.compose.viewmodel.koinViewModel

@Composable
internal fun HomeNavigation() {
    val viewModel = koinViewModel<HomeViewModel>()
    val state by viewModel.state.collectAsState()
    var activeEffect by remember { mutableStateOf<HomeEffect?>(null) }

    LaunchedEffect(viewModel) {
        viewModel.onIntent(HomeIntent.Load)
        viewModel.effects.collect { effect ->
            activeEffect = effect
        }
    }

    HomeScreen(
        state = state,
        onIntent = viewModel::onIntent,
    )

    when (val effect = activeEffect) {
        HomeEffect.OpenArchive ->
            PlaceholderEffectDialog(
                title = "Archive",
                body = "Archive is not yet implemented.",
                onDismiss = { activeEffect = null },
            )
        HomeEffect.OpenCreateHabit ->
            PlaceholderEffectDialog(
                title = "Create habit",
                body = "Create habit is not yet implemented.",
                onDismiss = { activeEffect = null },
            )
        is HomeEffect.OpenEditHabit ->
            PlaceholderEffectDialog(
                title = "Edit habit",
                body = "Edit habit ${effect.habitId.value} is not yet implemented.",
                onDismiss = { activeEffect = null },
            )
        null -> Unit
    }
}

@Composable
private fun PlaceholderEffectDialog(
    title: String,
    body: String,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = { Text(body) },
        confirmButton = {
            TextButton(onClick = onDismiss) { Text("Close") }
        },
    )
}
