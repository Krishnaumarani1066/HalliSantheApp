package com.example.hallisanthe.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColors = lightColorScheme(
    primary = DeepGreen,
    onPrimary = EarthWhite,
    secondary = LightGreen,
    background = SoftMint,
    surface = EarthWhite,
    surfaceVariant = SoftMint,
    error = ErrorRed,
    onBackground = TextPrimary,
    onSurface = TextPrimary
)

@Composable
fun HalliSantheTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColors,
        typography = AppTypography,
        content = content
    )
}
