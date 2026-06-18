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
import habitstracker.home_impl.generated.resources.Res
import habitstracker.home_impl.generated.resources.home_dialog_archive_body
import habitstracker.home_impl.generated.resources.home_dialog_archive_title
import habitstracker.home_impl.generated.resources.home_dialog_close
import org.jetbrains.compose.resources.stringResource
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

    if (activeEffect == HomeEffect.OpenArchive) {
        PlaceholderEffectDialog(
            title = stringResource(Res.string.home_dialog_archive_title),
            body = stringResource(Res.string.home_dialog_archive_body),
            onDismiss = { activeEffect = null },
        )
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
            TextButton(onClick = onDismiss) { Text(stringResource(Res.string.home_dialog_close)) }
        },
    )
}
