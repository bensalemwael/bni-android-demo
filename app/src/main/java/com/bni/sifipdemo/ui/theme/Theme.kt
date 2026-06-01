package com.bni.sifipdemo.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val BniLightColorScheme = lightColorScheme(
    primary = BniNavy,
    onPrimary = Color.White,
    primaryContainer = BniNavyLight,
    onPrimaryContainer = Color.White,
    secondary = BniGold,
    onSecondary = BniNavyDark,
    secondaryContainer = BniSand,
    onSecondaryContainer = BniNavyDark,
    tertiary = BniGoldDark,
    onTertiary = Color.White,
    background = BniSurface,
    onBackground = BniNavyDark,
    surface = Color.White,
    onSurface = BniNavyDark,
    surfaceVariant = BniSurface,
    onSurfaceVariant = BniNavy,
    error = StatusError,
    onError = Color.White,
)

@Composable
fun BniTheme(
    @Suppress("UNUSED_PARAMETER") darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    // BNI demo: corporate light scheme — keeps branding consistent in front
    // of the bank's stakeholders.
    MaterialTheme(
        colorScheme = BniLightColorScheme,
        typography = BniTypography,
        content = content,
    )
}
