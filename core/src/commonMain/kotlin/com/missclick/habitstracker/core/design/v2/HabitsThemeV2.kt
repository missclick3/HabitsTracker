package com.missclick.habitstracker.core.design.v2

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import habitstracker.core.generated.resources.Res
import habitstracker.core.generated.resources.manrope_bold
import habitstracker.core.generated.resources.manrope_regular
import habitstracker.core.generated.resources.manrope_semibold
import org.jetbrains.compose.resources.Font

// TODO: add dm_serif_display_regular.ttf + dm_sans_*.ttf to composeResources/font/
//       then replace manrope fallback with:
//         displayFamily = FontFamily(Font(Res.font.dm_serif_display_regular))
//         bodyFamily    = FontFamily(Font(Res.font.dm_sans_regular), ...)

private val LocalHabitsColorsV2 = staticCompositionLocalOf { HabitsCozyLightColors }
private val LocalHabitsTextStylesV2 = staticCompositionLocalOf {
    habitsTextStylesV2(FontFamily.Default, FontFamily.Default)
}
private val LocalHabitsDimensionsV2 = staticCompositionLocalOf { HabitsDefaultDimensionsV2 }

object HabitsThemeV2 {
    val colors: HabitsColorsV2
        @Composable get() = LocalHabitsColorsV2.current

    val textStyles: HabitsTextStylesV2
        @Composable get() = LocalHabitsTextStylesV2.current

    val dimensions: HabitsDimensionsV2
        @Composable get() = LocalHabitsDimensionsV2.current
}

@Composable
fun HabitsTrackerThemeV2(
    useDarkTheme: Boolean = false,
    content: @Composable () -> Unit,
) {
    val manrope =
        FontFamily(
            Font(resource = Res.font.manrope_regular, weight = FontWeight.Normal),
            Font(resource = Res.font.manrope_semibold, weight = FontWeight.SemiBold),
            Font(resource = Res.font.manrope_bold, weight = FontWeight.Bold),
        )

    val colors = if (useDarkTheme) HabitsCozyDarkColors else HabitsCozyLightColors
    val textStyles = habitsTextStylesV2(displayFamily = manrope, bodyFamily = manrope)

    MaterialTheme(
        colorScheme =
            if (useDarkTheme) {
                darkColorScheme(
                    primary = colors.accent,
                    onPrimary = colors.onAccent,
                    background = colors.background,
                    surface = colors.surface,
                    onSurface = colors.text,
                    surfaceVariant = colors.surfaceAlt,
                    onSurfaceVariant = colors.textMuted,
                    outline = colors.border,
                    error = colors.danger,
                    onError = colors.onAccent,
                )
            } else {
                lightColorScheme(
                    primary = colors.accent,
                    onPrimary = colors.onAccent,
                    background = colors.background,
                    surface = colors.surface,
                    onSurface = colors.text,
                    surfaceVariant = colors.surfaceAlt,
                    onSurfaceVariant = colors.textMuted,
                    outline = colors.border,
                    error = colors.danger,
                    onError = colors.onAccent,
                )
            },
    ) {
        CompositionLocalProvider(
            LocalHabitsColorsV2 provides colors,
            LocalHabitsTextStylesV2 provides textStyles,
            LocalHabitsDimensionsV2 provides HabitsDefaultDimensionsV2,
            content = content,
        )
    }
}