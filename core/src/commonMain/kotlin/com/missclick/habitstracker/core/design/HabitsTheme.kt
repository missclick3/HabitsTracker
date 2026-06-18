package com.missclick.habitstracker.core.design

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

private val LocalHabitsColors = staticCompositionLocalOf { HabitsLightColors }
private val LocalHabitsTextStyles = staticCompositionLocalOf {
    habitsTextStyles(FontFamily.Default, FontFamily.Default)
}
private val LocalHabitsDimensions = staticCompositionLocalOf { HabitsDefaultDimensions }

object HabitsTheme {
    val colors: HabitsColors
        @Composable get() = LocalHabitsColors.current

    val textStyles: HabitsTextStyles
        @Composable get() = LocalHabitsTextStyles.current

    val dimensions: HabitsDimensions
        @Composable get() = LocalHabitsDimensions.current
}

@Composable
fun HabitsTrackerTheme(
    useDarkTheme: Boolean = false,
    content: @Composable () -> Unit,
) {
    val manrope =
        FontFamily(
            Font(resource = Res.font.manrope_regular, weight = FontWeight.Normal),
            Font(resource = Res.font.manrope_semibold, weight = FontWeight.SemiBold),
            Font(resource = Res.font.manrope_bold, weight = FontWeight.Bold),
        )

    val colors = if (useDarkTheme) HabitsDarkColors else HabitsLightColors
    val textStyles = habitsTextStyles(displayFamily = manrope, bodyFamily = manrope)

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
            LocalHabitsColors provides colors,
            LocalHabitsTextStyles provides textStyles,
            LocalHabitsDimensions provides HabitsDefaultDimensions,
            content = content,
        )
    }
}
