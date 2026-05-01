package com.missclick.habitstracker

import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavKey
import com.missclick.habitstracker.core.navigation.FeatureEntryBuilder

@Composable
expect fun AppNavHost(
    modifier: Modifier,
    backStack: SnapshotStateList<NavKey>,
    onBack: () -> Boolean,
    builders: List<FeatureEntryBuilder>,
)
