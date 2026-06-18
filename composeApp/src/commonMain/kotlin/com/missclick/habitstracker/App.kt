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
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.missclick.habitstracker.core.design.HabitsTheme
import com.missclick.habitstracker.core.design.HabitsTrackerTheme
import com.missclick.habitstracker.core.navigation.FeatureEntryBuilder
import com.missclick.habitstracker.core.navigation.Navigator
import com.missclick.habitstracker.core.navigation.createNavigator
import com.missclick.habitstracker.home.api.HomeScreenRoute
import com.missclick.habitstracker.home.impl.di.homeFeatureModule
import habitstracker.composeapp.generated.resources.Res
import habitstracker.composeapp.generated.resources.app_bottom_home
import habitstracker.composeapp.generated.resources.app_bottom_journal
import habitstracker.composeapp.generated.resources.app_journal_placeholder_body
import habitstracker.composeapp.generated.resources.app_journal_placeholder_title
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.KoinApplication
import org.koin.compose.getKoin
import org.koin.compose.koinInject
import org.koin.dsl.koinConfiguration
import org.koin.dsl.module

@Composable
@Preview
fun App() {
    val configuration =
        koinConfiguration {
            modules(
                module { single<Navigator> { createNavigator(start = HomeScreenRoute.HomeScreen) } },
                homeFeatureModule,
            )
        }

    KoinApplication(
        configuration = configuration,
        content = {
            val navigator: Navigator = koinInject()
            val builders: List<FeatureEntryBuilder> = getKoin().getAll()
            val backStack = navigator.backStack
            val selectedTab = backStack.lastOrNull()?.toBottomTabOrNull() ?: BottomTab.HOME

            HabitsTrackerTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = HabitsTheme.colors.background,
                    bottomBar = {
                        AppBottomBar(
                            selectedTab = selectedTab,
                            onTabSelected = { tab ->
                                val route = tab.toRoute()
                                val hasRoot = backStack.any { it == route }
                                when {
                                    selectedTab == tab -> navigator.popUpTo { it == route }
                                    hasRoot -> navigator.popUpTo { it == route }
                                    else -> navigator.navigate(route)
                                }
                            },
                            onAddClicked = {},
                        )
                    },
                ) { padding ->
                    val journalEntry: FeatureEntryBuilder = {
                        entry<JournalRoute> { _ ->
                            PlaceholderScreen(
                                modifier =
                                    Modifier
                                        .fillMaxSize()
                                        .background(HabitsTheme.colors.background),
                                title = stringResource(Res.string.app_journal_placeholder_title),
                                body = stringResource(Res.string.app_journal_placeholder_body),
                            )
                        }
                    }
                    AppNavHost(
                        modifier =
                            Modifier
                                .fillMaxSize()
                                .padding(padding),
                        backStack = backStack,
                        onBack = { navigator.back() },
                        builders = builders + journalEntry,
                    )
                }
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
        modifier =
            Modifier
                .fillMaxWidth()
                .height(104.dp),
    ) {
        Surface(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter),
            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
            shadowElevation = 18.dp,
            color = HabitsTheme.colors.surface,
        ) {
            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = HabitsTheme.dimensions.spacingXxl, vertical = 18.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                BottomBarItem(
                    tab = BottomTab.HOME,
                    label = stringResource(Res.string.app_bottom_home),
                    selected = selectedTab == BottomTab.HOME,
                    onClick = { onTabSelected(BottomTab.HOME) },
                )
                Spacer(modifier = Modifier.size(72.dp))
                BottomBarItem(
                    tab = BottomTab.JOURNAL,
                    label = stringResource(Res.string.app_bottom_journal),
                    selected = selectedTab == BottomTab.JOURNAL,
                    onClick = { onTabSelected(BottomTab.JOURNAL) },
                )
            }
        }

        Surface(
            modifier =
                Modifier
                    .size(72.dp)
                    .align(Alignment.TopCenter)
                    .clip(CircleShape)
                    .clickable(onClick = onAddClicked),
            shape = CircleShape,
            color = HabitsTheme.colors.accent,
            shadowElevation = 16.dp,
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = "+",
                    style = HabitsTheme.textStyles.displayMedium,
                    color = HabitsTheme.colors.onAccent,
                )
            }
        }
    }
}

@Composable
private fun BottomBarItem(
    tab: BottomTab,
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Column(
        modifier = Modifier.clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Icon(
            imageVector = tab.icon,
            contentDescription = label,
            tint = if (selected) HabitsTheme.colors.accent else HabitsTheme.colors.textFaint,
            modifier = Modifier.size(24.dp)
        )
        Text(
            text = label,
            color = if (selected) HabitsTheme.colors.accent else HabitsTheme.colors.textFaint,
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
        modifier =
            modifier
                .background(HabitsTheme.colors.background)
                .padding(24.dp),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(HabitsTheme.dimensions.spacingMd),
        ) {
            Text(
                text = title,
                style = HabitsTheme.textStyles.displayMedium,
                color = HabitsTheme.colors.text,
                textAlign = TextAlign.Center,
            )
            Text(
                text = body,
                style = HabitsTheme.textStyles.body,
                color = HabitsTheme.colors.textFaint,
                textAlign = TextAlign.Center,
            )
        }
    }
}
