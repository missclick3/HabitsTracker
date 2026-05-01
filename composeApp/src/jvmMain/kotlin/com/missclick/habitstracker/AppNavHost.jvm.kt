package com.missclick.habitstracker

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import com.missclick.habitstracker.core.navigation.FeatureEntryBuilder

@Composable
actual fun AppNavHost(
    modifier: Modifier,
    backStack: SnapshotStateList<NavKey>,
    onBack: () -> Boolean,
    builders: List<FeatureEntryBuilder>,
) {
    val provider = remember(builders) { entryProvider<NavKey> { builders.forEach { it() } } }
    val currentKey = backStack.lastOrNull() ?: return
    val entry = provider(currentKey)
    Box(modifier = modifier) {
        entry.Content()
    }
}
