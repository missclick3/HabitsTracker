package com.missclick.habitstracker.core.design

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import habitstracker.core.generated.resources.Res
import habitstracker.core.generated.resources.manrope_bold
import habitstracker.core.generated.resources.manrope_regular
import habitstracker.core.generated.resources.manrope_semibold
import org.jetbrains.compose.resources.Font

@Immutable
data class HabitColors(
    val brandPrimary: Color,
    val brandPrimarySoft: Color,
    val brandPrimarySoftSelected: Color,
    val background: Color,
    val surface: Color,
    val surfaceSubtle: Color,
    val textPrimary: Color,
    val textStrong: Color,
    val textSecondary: Color,
    val textMuted: Color,
    val textHint: Color,
    val textTertiary: Color,
    val textInactive: Color,
    val success: Color,
    val onBrand: Color,
    val border: Color,
)

@Immutable
data class HabitTextStyles(
    val screenTitle: TextStyle,
    val screenSubtitle: TextStyle,
    val sectionLabel: TextStyle,
    val cardTitle: TextStyle,
    val body: TextStyle,
    val bodyMuted: TextStyle,
    val actionLabel: TextStyle,
    val chipLabel: TextStyle,
    val supporting: TextStyle,
    val metricValue: TextStyle,
    val buttonLabel: TextStyle,
)

private val LightHabitColors = HabitColors(
    brandPrimary = Color(0xFF7E5BFF),
    brandPrimarySoft = Color(0xFFF0EEFF),
    brandPrimarySoftSelected = Color(0xFFF1ECFF),
    background = Color(0xFFF8F7FB),
    surface = Color.White,
    surfaceSubtle = Color(0xFFF7F7FB),
    textPrimary = Color(0xFF12182C),
    textStrong = Color(0xFF1A2036),
    textSecondary = Color(0xFF2B3551),
    textMuted = Color(0xFF6E7894),
    textHint = Color(0xFF66708B),
    textTertiary = Color(0xFF8D98B3),
    textInactive = Color(0xFF9AA4BD),
    success = Color(0xFF18A872),
    onBrand = Color.White,
    border = Color(0xFF8D98B3),
)

private val DarkHabitColors = LightHabitColors

private val LocalHabitColors = staticCompositionLocalOf { LightHabitColors }
private val LocalHabitTextStyles = staticCompositionLocalOf { defaultTextStyles(Typography()) }

object HabitsTheme {
    val colors: HabitColors
        @Composable get() = LocalHabitColors.current

    val textStyles: HabitTextStyles
        @Composable get() = LocalHabitTextStyles.current
}

@Composable
fun HabitsTrackerTheme(
    useDarkTheme: Boolean = false,
    content: @Composable () -> Unit,
) {
    val colors = if (useDarkTheme) DarkHabitColors else LightHabitColors
    val typography = Typography().withDefaultFontFamily(
        fontFamily = FontFamily(
            Font(resource = Res.font.manrope_regular, weight = FontWeight.Normal),
            Font(resource = Res.font.manrope_semibold, weight = FontWeight.SemiBold),
            Font(resource = Res.font.manrope_bold, weight = FontWeight.Bold),
        ),
    )

    MaterialTheme(
        colorScheme = if (useDarkTheme) {
            darkColorScheme(
                primary = colors.brandPrimary,
                onPrimary = colors.onBrand,
                secondary = colors.textTertiary,
                background = colors.background,
                surface = colors.surface,
                onSurface = colors.textPrimary,
                surfaceVariant = colors.surfaceSubtle,
                onSurfaceVariant = colors.textSecondary,
                outline = colors.border,
                tertiary = colors.success,
                onTertiary = colors.onBrand,
            )
        } else {
            lightColorScheme(
                primary = colors.brandPrimary,
                onPrimary = colors.onBrand,
                secondary = colors.textTertiary,
                background = colors.background,
                surface = colors.surface,
                onSurface = colors.textPrimary,
                surfaceVariant = colors.surfaceSubtle,
                onSurfaceVariant = colors.textSecondary,
                outline = colors.border,
                tertiary = colors.success,
                onTertiary = colors.onBrand,
            )
        },
        typography = typography,
    ) {
        CompositionLocalProvider(
            LocalHabitColors provides colors,
            LocalHabitTextStyles provides defaultTextStyles(typography),
            content = content,
        )
    }
}

private fun Typography.withDefaultFontFamily(fontFamily: FontFamily): Typography = Typography(
    displayLarge = displayLarge.copy(fontFamily = fontFamily),
    displayMedium = displayMedium.copy(fontFamily = fontFamily),
    displaySmall = displaySmall.copy(fontFamily = fontFamily),
    headlineLarge = headlineLarge.copy(fontFamily = fontFamily),
    headlineMedium = headlineMedium.copy(fontFamily = fontFamily),
    headlineSmall = headlineSmall.copy(fontFamily = fontFamily),
    titleLarge = titleLarge.copy(fontFamily = fontFamily),
    titleMedium = titleMedium.copy(fontFamily = fontFamily),
    titleSmall = titleSmall.copy(fontFamily = fontFamily),
    bodyLarge = bodyLarge.copy(fontFamily = fontFamily),
    bodyMedium = bodyMedium.copy(fontFamily = fontFamily),
    bodySmall = bodySmall.copy(fontFamily = fontFamily),
    labelLarge = labelLarge.copy(fontFamily = fontFamily),
    labelMedium = labelMedium.copy(fontFamily = fontFamily),
    labelSmall = labelSmall.copy(fontFamily = fontFamily),
)

private fun defaultTextStyles(typography: Typography): HabitTextStyles = HabitTextStyles(
    screenTitle = typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
    screenSubtitle = typography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
    sectionLabel = typography.labelLarge.copy(fontWeight = FontWeight.SemiBold),
    cardTitle = typography.titleLarge.copy(fontWeight = FontWeight.Bold),
    body = typography.bodyMedium,
    bodyMuted = typography.bodyMedium,
    actionLabel = typography.labelLarge.copy(fontWeight = FontWeight.SemiBold),
    chipLabel = typography.titleMedium.copy(fontWeight = FontWeight.Bold),
    supporting = typography.bodyMedium,
    metricValue = typography.titleMedium.copy(fontWeight = FontWeight.Bold),
    buttonLabel = typography.labelLarge.copy(fontWeight = FontWeight.SemiBold),
)
