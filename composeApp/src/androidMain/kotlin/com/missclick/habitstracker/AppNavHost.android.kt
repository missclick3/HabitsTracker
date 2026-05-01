package com.missclick.habitstracker

import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.missclick.habitstracker.core.navigation.FeatureEntryBuilder

@Composable
actual fun AppNavHost(
    modifier: Modifier,
    backStack: SnapshotStateList<NavKey>,
    onBack: () -> Boolean,
    builders: List<FeatureEntryBuilder>,
) {
    NavDisplay(
        modifier = modifier,
        backStack = backStack,
        onBack = { onBack() },
        entryDecorators = listOf(rememberSaveableStateHolderNavEntryDecorator()),
        entryProvider = entryProvider { builders.forEach { it() } },
    )
}
