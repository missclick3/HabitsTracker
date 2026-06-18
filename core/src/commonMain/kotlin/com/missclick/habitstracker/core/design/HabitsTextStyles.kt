package com.missclick.habitstracker.core.design

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Immutable
data class HabitsTextStyles(
    val displayLarge: TextStyle,
    val displayMedium: TextStyle,
    val displaySmall: TextStyle,
    val body: TextStyle,
    val bodyMedium: TextStyle,
    val label: TextStyle,
    val caption: TextStyle,
    val metric: TextStyle,
    val buttonLabel: TextStyle,
    val navLabel: TextStyle,
)

fun habitsTextStyles(
    displayFamily: FontFamily,
    bodyFamily: FontFamily,
): HabitsTextStyles =
    HabitsTextStyles(
        displayLarge =
            TextStyle(
                fontFamily = displayFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 28.sp,
                lineHeight = 32.sp,
                letterSpacing = (-0.5).sp,
            ),
        displayMedium =
            TextStyle(
                fontFamily = displayFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 22.sp,
                lineHeight = 26.sp,
            ),
        displaySmall =
            TextStyle(
                fontFamily = displayFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp,
                lineHeight = 22.sp,
            ),
        body =
            TextStyle(
                fontFamily = bodyFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                lineHeight = 20.sp,
            ),
        bodyMedium =
            TextStyle(
                fontFamily = bodyFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                lineHeight = 20.sp,
            ),
        label =
            TextStyle(
                fontFamily = bodyFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 11.sp,
                lineHeight = 16.sp,
                letterSpacing = 0.4.sp,
            ),
        caption =
            TextStyle(
                fontFamily = bodyFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 12.sp,
                lineHeight = 17.sp,
            ),
        metric =
            TextStyle(
                fontFamily = displayFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 28.sp,
                lineHeight = 32.sp,
                letterSpacing = (-0.5).sp,
            ),
        buttonLabel =
            TextStyle(
                fontFamily = bodyFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                lineHeight = 20.sp,
            ),
        navLabel =
            TextStyle(
                fontFamily = bodyFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp,
                lineHeight = 15.sp,
                letterSpacing = 0.4.sp,
            ),
    )
