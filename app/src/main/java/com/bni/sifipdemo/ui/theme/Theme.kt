package com.bni.sifipdemo.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val BniLightColorScheme = lightColorScheme(
    primary = BniGreen,
    onPrimary = Color.White,
    primaryContainer = BniGreenLight,
    onPrimaryContainer = BniGreenDeep,
    secondary = BniOrange,
    onSecondary = Color.White,
    secondaryContainer = BniGreenTint,
    onSecondaryContainer = BniGreenDeep,
    tertiary = BniOrangeDark,
    onTertiary = Color.White,
    background = BniBackground,
    onBackground = BniText,
    surface = Color.White,
    onSurface = BniText,
    surfaceVariant = BniGreenTint,
    onSurfaceVariant = BniGreenDeep,
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
