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
import androidx.compose.ui.unit.sp
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
    val moodDrained: Color,
    val moodLow: Color,
    val moodNeutral: Color,
    val moodGood: Color,
    val moodGreat: Color,
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
    val headerOverline: TextStyle,
    val sectionOverline: TextStyle,
    val headerName: TextStyle,
    val headerDate: TextStyle,
    val bodyPrompt: TextStyle,
    val bodyText: TextStyle,
    val bodyTextMuted: TextStyle,
    val cardTitle: TextStyle,
    val actionLabel: TextStyle,
    val navLabel: TextStyle,
    val metricValue: TextStyle,
    val buttonLabel: TextStyle,
)

internal val HabitsLightColors =
    HabitColors(
        brandPrimary = Color(0xFF7950F2),
        brandPrimarySoft = Color(0xFFF3F0FF),
        brandPrimarySoftSelected = Color(0xFFF3F0FF),
        moodDrained = Color(0xFFFF2D3A),
        moodLow = Color(0xFFFF9C2F),
        moodNeutral = Color(0xFFF7EE2B),
        moodGood = Color(0xFF7EE22E),
        moodGreat = Color(0xFF14DE14),
        background = Color(0xFFF8FAFC),
        surface = Color.White,
        surfaceSubtle = Color(0xFFF8FAFC),
        textPrimary = Color(0xFF0F172A),
        textStrong = Color(0xFF1E293B),
        textSecondary = Color(0xFF64748B),
        textMuted = Color(0xFF94A3B8),
        textHint = Color(0xFF94A3B8),
        textTertiary = Color(0xFF94A3B8),
        textInactive = Color(0xFF94A3B8),
        success = Color(0xFF0CA678),
        onBrand = Color.White,
        border = Color(0xFFF1F5F9),
    )

internal val HabitsDarkColors =
    HabitColors(
        brandPrimary = Color(0xFF7950F2),
        brandPrimarySoft = Color(0xFFF3F0FF),
        brandPrimarySoftSelected = Color(0xFFF3F0FF),
        moodDrained = Color(0xFFFF2D3A),
        moodLow = Color(0xFFFF9C2F),
        moodNeutral = Color(0xFFF7EE2B),
        moodGood = Color(0xFF7EE22E),
        moodGreat = Color(0xFF14DE14),
        background = Color(0xFFF8FAFC),
        surface = Color.White,
        surfaceSubtle = Color(0xFFF8FAFC),
        textPrimary = Color(0xFF0F172A),
        textStrong = Color(0xFF1E293B),
        textSecondary = Color(0xFF64748B),
        textMuted = Color(0xFF94A3B8),
        textHint = Color(0xFF94A3B8),
        textTertiary = Color(0xFF94A3B8),
        textInactive = Color(0xFF94A3B8),
        success = Color(0xFF0CA678),
        onBrand = Color.White,
        border = Color(0xFFF1F5F9),
    )

private val LocalHabitColors = staticCompositionLocalOf { HabitsLightColors }
private val LocalHabitTextStyles = staticCompositionLocalOf { habitsTextStyles(Typography()) }

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
    val colors = if (useDarkTheme) HabitsDarkColors else HabitsLightColors
    val typography =
        Typography().withDefaultFontFamily(
            fontFamily =
                FontFamily(
                    Font(resource = Res.font.manrope_regular, weight = FontWeight.Normal),
                    Font(resource = Res.font.manrope_semibold, weight = FontWeight.SemiBold),
                    Font(resource = Res.font.manrope_bold, weight = FontWeight.Bold),
                ),
        )

    MaterialTheme(
        colorScheme =
            if (useDarkTheme) {
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
            LocalHabitTextStyles provides habitsTextStyles(typography),
            content = content,
        )
    }
}

private fun Typography.withDefaultFontFamily(fontFamily: FontFamily): Typography =
    Typography(
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

internal fun habitsTextStyles(typography: Typography): HabitTextStyles =
    HabitTextStyles(
        headerOverline =
            typography.labelSmall.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp,
                lineHeight = 16.5.sp,
                letterSpacing = 1.1.sp,
            ),
        sectionOverline =
            typography.labelLarge.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                lineHeight = 18.sp,
                letterSpacing = 2.4.sp,
            ),
        headerName =
            typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                lineHeight = 28.sp,
                letterSpacing = (-0.5).sp,
            ),
        headerDate =
            typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 30.sp,
                lineHeight = 36.sp,
                letterSpacing = (-0.75).sp,
            ),
        bodyPrompt =
            typography.bodyLarge.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                lineHeight = 24.sp,
                letterSpacing = 0.sp,
            ),
        bodyText =
            typography.bodyMedium.copy(
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                lineHeight = 24.sp,
                letterSpacing = 0.sp,
            ),
        bodyTextMuted =
            typography.bodyMedium.copy(
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                lineHeight = 24.sp,
                letterSpacing = 0.sp,
            ),
        cardTitle =
            typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                lineHeight = 20.sp,
                letterSpacing = 0.sp,
            ),
        actionLabel =
            typography.labelLarge.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                lineHeight = 16.sp,
                letterSpacing = 0.sp,
            ),
        navLabel =
            typography.labelSmall.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 10.sp,
                lineHeight = 15.sp,
                letterSpacing = 1.sp,
            ),
        metricValue =
            typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                lineHeight = 32.sp,
                letterSpacing = 0.sp,
            ),
        buttonLabel =
            typography.labelLarge.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                lineHeight = 20.sp,
                letterSpacing = 0.sp,
            ),
    )
