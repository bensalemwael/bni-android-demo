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
    secondary = BniRed,
    onSecondary = Color.White,
    secondaryContainer = BniSand,
    onSecondaryContainer = BniNavyDark,
    tertiary = BniRedDark,
    onTertiary = Color.White,
    background = BniBackground,
    onBackground = BniText,
    surface = Color.White,
    onSurface = BniText,
    surfaceVariant = BniBackground,
    onSurfaceVariant = BniNavy,
    outline = BniBorder,
    error = StatusError,
    onError = Color.White,
)

@Composable
fun BniTheme(
    @Suppress("UNUSED_PARAMETER") darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = BniLightColorScheme,
        typography = BniTypography,
        content = content,
    )
}
