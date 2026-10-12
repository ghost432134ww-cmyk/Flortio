package com.hnfak.flortio.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/** Flortio renk paleti. Metin renkleri koyu zeminlerde en az WCAG AA kontrastı sağlar. */
object FlortioColors {
    val Background = Color(0xFF0D0B12)
    val Card = Color(0xFF191522)
    val CardHigh = Color(0xFF231D31)
    val Purple = Color(0xFF9B5CFF)
    val Lavender = Color(0xFFC7A7FF)
    val TextPrimary = Color(0xFFF4F0FA)
    val TextSecondary = Color(0xFFB9B0C9)
    val Outline = Color(0xFF3B3350)
    val OutlineSoft = Color(0xFF2A2338)
    val Error = Color(0xFFFF7A90)
    val ErrorContainer = Color(0xFF3A1621)
    val Success = Color(0xFF6EE7B7)
    val SuccessContainer = Color(0xFF12281F)
    val GlowTop = Color(0xFF2A1650)
    val InfoContainer = Color(0xFF211A33)
    val DisabledContainer = Color(0xFF2A2438)
    val DisabledContent = Color(0xFF8C84A0)
}

private val FlortioDarkScheme = darkColorScheme(
    primary = FlortioColors.Purple,
    onPrimary = FlortioColors.Background,
    primaryContainer = Color(0xFF2A1F45),
    onPrimaryContainer = FlortioColors.Lavender,
    secondary = FlortioColors.Lavender,
    onSecondary = FlortioColors.Background,
    secondaryContainer = Color(0xFF2A2240),
    onSecondaryContainer = FlortioColors.Lavender,
    background = FlortioColors.Background,
    onBackground = FlortioColors.TextPrimary,
    surface = FlortioColors.Card,
    onSurface = FlortioColors.TextPrimary,
    surfaceVariant = FlortioColors.CardHigh,
    onSurfaceVariant = FlortioColors.TextSecondary,
    surfaceContainerLowest = FlortioColors.Background,
    surfaceContainerLow = FlortioColors.Card,
    surfaceContainer = FlortioColors.Card,
    surfaceContainerHigh = FlortioColors.CardHigh,
    surfaceContainerHighest = FlortioColors.CardHigh,
    outline = FlortioColors.Outline,
    outlineVariant = FlortioColors.OutlineSoft,
    error = FlortioColors.Error,
    onError = FlortioColors.Background,
    errorContainer = FlortioColors.ErrorContainer,
    onErrorContainer = Color(0xFFFFB3C0),
)

private val FlortioTypography = Typography(
    displaySmall = TextStyle(
        fontWeight = FontWeight.Bold, fontSize = 34.sp, lineHeight = 40.sp, letterSpacing = (-0.5).sp,
    ),
    headlineMedium = TextStyle(
        fontWeight = FontWeight.Bold, fontSize = 28.sp, lineHeight = 34.sp, letterSpacing = (-0.25).sp,
    ),
    headlineSmall = TextStyle(
        fontWeight = FontWeight.SemiBold, fontSize = 22.sp, lineHeight = 28.sp,
    ),
    titleLarge = TextStyle(
        fontWeight = FontWeight.SemiBold, fontSize = 20.sp, lineHeight = 26.sp,
    ),
    titleMedium = TextStyle(
        fontWeight = FontWeight.SemiBold, fontSize = 16.sp, lineHeight = 22.sp, letterSpacing = 0.1.sp,
    ),
    bodyLarge = TextStyle(
        fontWeight = FontWeight.Normal, fontSize = 16.sp, lineHeight = 24.sp, letterSpacing = 0.2.sp,
    ),
    bodyMedium = TextStyle(
        fontWeight = FontWeight.Normal, fontSize = 14.sp, lineHeight = 21.sp, letterSpacing = 0.2.sp,
    ),
    labelLarge = TextStyle(
        fontWeight = FontWeight.SemiBold, fontSize = 15.sp, lineHeight = 20.sp, letterSpacing = 0.2.sp,
    ),
)

private val FlortioShapes = Shapes(
    extraSmall = RoundedCornerShape(8.dp),
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(24.dp),
    extraLarge = RoundedCornerShape(28.dp),
)

@Composable
fun FlortioTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = FlortioDarkScheme,
        typography = FlortioTypography,
        shapes = FlortioShapes,
        content = content,
    )
}
