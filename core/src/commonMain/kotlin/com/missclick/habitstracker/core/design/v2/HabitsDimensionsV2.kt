package com.missclick.habitstracker.core.design.v2

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
data class HabitsDimensionsV2(
    val spacingXs: Dp,
    val spacingSm: Dp,
    val spacingMd: Dp,
    val spacingLg: Dp,
    val spacingXl: Dp,
    val spacingXxl: Dp,
    val radiusXs: Dp,
    val radiusSm: Dp,
    val radiusMd: Dp,
    val radiusLg: Dp,
    val radiusCard: Dp,
    val radiusPill: Dp,
    val pagePaddingHorizontal: Dp,
    val cardPaddingInternal: Dp,
    val cardGap: Dp,
)

val HabitsDefaultDimensionsV2 =
    HabitsDimensionsV2(
        spacingXs = 4.dp,
        spacingSm = 8.dp,
        spacingMd = 12.dp,
        spacingLg = 16.dp,
        spacingXl = 20.dp,
        spacingXxl = 28.dp,
        radiusXs = 6.dp,
        radiusSm = 8.dp,
        radiusMd = 12.dp,
        radiusLg = 14.dp,
        radiusCard = 22.dp,
        radiusPill = 999.dp,
        pagePaddingHorizontal = 20.dp,
        cardPaddingInternal = 20.dp,
        cardGap = 16.dp,
    )