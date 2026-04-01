package com.missclick.habitstracker

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.missclick.habitstracker.core.design.HabitsTheme
import com.missclick.habitstracker.core.design.HabitsTrackerTheme
import androidx.navigation3.runtime.NavBackStack
import com.missclick.habitstracker.core.navigation.AppComposeNavigator
import com.missclick.habitstracker.core.navigation.AppScreen
import com.missclick.habitstracker.core.navigation.JournalRoute
import com.missclick.habitstracker.core.navigation.NavigationResults
import com.missclick.habitstracker.core.navigation.navigationModule
import com.missclick.habitstracker.home.api.HomeCallbacks
import com.missclick.habitstracker.home.api.HomeFeatureApi
import com.missclick.habitstracker.home.api.HomeRoute
import com.missclick.habitstracker.home.impl.di.homeFeatureModule
import habitstracker.composeapp.generated.resources.Res
import habitstracker.composeapp.generated.resources.app_bottom_home
import habitstracker.composeapp.generated.resources.app_bottom_journal
import habitstracker.composeapp.generated.resources.app_dialog_archive_body
import habitstracker.composeapp.generated.resources.app_dialog_archive_title
import habitstracker.composeapp.generated.resources.app_dialog_close
import habitstracker.composeapp.generated.resources.app_dialog_create_body
import habitstracker.composeapp.generated.resources.app_dialog_create_title
import habitstracker.composeapp.generated.resources.app_dialog_edit_body
import habitstracker.composeapp.generated.resources.app_dialog_edit_title
import habitstracker.composeapp.generated.resources.app_journal_placeholder_body
import habitstracker.composeapp.generated.resources.app_journal_placeholder_title
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.KoinApplication
import org.koin.compose.koinInject
import org.koin.dsl.koinConfiguration

private sealed interface AppDialog {
    data object CreateHabit : AppDialog
    data object Archive : AppDialog
    data class EditHabit(val habitId: String) : AppDialog
}

@Composable
@Preview
fun App() {
    KoinApplication(
        configuration = koinConfiguration(
            declaration = {
                modules(
                    navigationModule,
                    homeFeatureModule,
                )
            },
        ),
        content = {
            val homeFeature: HomeFeatureApi = koinInject()
            val navigator: AppComposeNavigator = koinInject()
            val results: NavigationResults = koinInject()
            val backStack = remember { NavBackStack<AppScreen>(HomeRoute) }
            var dialog by remember { mutableStateOf<AppDialog?>(null) }

            LaunchedEffect(navigator, results) {
                navigator.bind(
                    backStack = backStack,
                    results = results,
                )
            }

            val selectedTab = backStack.lastOrNull()?.toBottomTabOrNull() ?: BottomTab.HOME

            HabitsTrackerTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = HabitsTheme.colors.background,
                    bottomBar = {
                        AppBottomBar(
                            selectedTab = selectedTab,
                            onTabSelected = { tab ->
                                val route = when (tab) {
                                    BottomTab.HOME -> HomeRoute
                                    BottomTab.JOURNAL -> JournalRoute
                                }
                                val hasRoot = backStack.any { key -> key == route }
                                when {
                                    selectedTab == tab -> navigator.popUpTo(route, inclusive = false)
                                    hasRoot -> navigator.popUpTo(route, inclusive = false)
                                    else -> navigator.navigate(route)
                                }
                            },
                            onAddClicked = { dialog = AppDialog.CreateHabit },
                        )
                    },
                ) { padding ->
                    val route = backStack.lastOrNull() ?: HomeRoute
                    when (route) {
                        HomeRoute -> homeFeature.Screen(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(padding)
                                .background(HabitsTheme.colors.background),
                            callbacks = HomeCallbacks(
                                onOpenArchive = { dialog = AppDialog.Archive },
                                onOpenCreateHabit = { dialog = AppDialog.CreateHabit },
                                onOpenEditHabit = { habitId ->
                                    dialog = AppDialog.EditHabit(habitId.value)
                                },
                            ),
                        )

                        JournalRoute -> PlaceholderScreen(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(padding)
                                .background(HabitsTheme.colors.background),
                            title = stringResource(Res.string.app_journal_placeholder_title),
                            body = stringResource(Res.string.app_journal_placeholder_body),
                        )
                    }
                }

                PlaceholderDialog(
                    dialog = dialog,
                    onDismiss = { dialog = null },
                )
            }
        },
    )
}

@Composable
private fun AppBottomBar(
    selectedTab: BottomTab,
    onTabSelected: (BottomTab) -> Unit,
    onAddClicked: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(104.dp),
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
            shadowElevation = 18.dp,
            color = HabitsTheme.colors.surface,
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 28.dp, vertical = 18.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                BottomBarItem(
                    label = stringResource(Res.string.app_bottom_home),
                    selected = selectedTab == BottomTab.HOME,
                    onClick = { onTabSelected(BottomTab.HOME) },
                )
                Spacer(modifier = Modifier.size(72.dp))
                BottomBarItem(
                    label = stringResource(Res.string.app_bottom_journal),
                    selected = selectedTab == BottomTab.JOURNAL,
                    onClick = { onTabSelected(BottomTab.JOURNAL) },
                )
            }
        }

        Surface(
            modifier = Modifier
                .size(72.dp)
                .align(Alignment.TopCenter)
                .clip(CircleShape)
                .clickable(onClick = onAddClicked),
            shape = CircleShape,
            color = HabitsTheme.colors.brandPrimary,
            shadowElevation = 16.dp,
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = "+",
                    style = HabitsTheme.textStyles.headerName,
                    color = HabitsTheme.colors.onBrand,
                )
            }
        }
    }
}

@Composable
private fun BottomBarItem(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Column(
        modifier = Modifier.clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Text(
            text = if (selected) "*" else "o",
            color = if (selected) HabitsTheme.colors.brandPrimary else HabitsTheme.colors.textInactive,
        )
        Text(
            text = label,
            color = if (selected) HabitsTheme.colors.brandPrimary else HabitsTheme.colors.textInactive,
            style = HabitsTheme.textStyles.navLabel,
        )
    }
}

@Composable
private fun PlaceholderScreen(
    modifier: Modifier = Modifier,
    title: String,
    body: String,
) {
    Box(
        modifier = modifier
            .background(HabitsTheme.colors.background)
            .padding(24.dp),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                text = title,
                style = HabitsTheme.textStyles.headerName,
                color = HabitsTheme.colors.textPrimary,
                textAlign = TextAlign.Center,
            )
            Text(
                text = body,
                style = HabitsTheme.textStyles.bodyTextMuted,
                color = HabitsTheme.colors.textHint,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
private fun PlaceholderDialog(
    dialog: AppDialog?,
    onDismiss: () -> Unit,
) {
    if (dialog == null) {
        return
    }

    val (title, message) = when (dialog) {
        AppDialog.Archive -> stringResource(Res.string.app_dialog_archive_title) to
            stringResource(Res.string.app_dialog_archive_body)
        AppDialog.CreateHabit -> stringResource(Res.string.app_dialog_create_title) to
            stringResource(Res.string.app_dialog_create_body)
        is AppDialog.EditHabit -> stringResource(Res.string.app_dialog_edit_title) to
            stringResource(Res.string.app_dialog_edit_body, dialog.habitId)
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = title,
                style = HabitsTheme.textStyles.cardTitle,
                color = HabitsTheme.colors.textPrimary,
            )
        },
        text = {
            Text(
                text = message,
                style = HabitsTheme.textStyles.bodyText,
                color = HabitsTheme.colors.textMuted,
            )
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = stringResource(Res.string.app_dialog_close),
                    style = HabitsTheme.textStyles.buttonLabel,
                    color = HabitsTheme.colors.brandPrimary,
                )
            }
        },
    )
}
