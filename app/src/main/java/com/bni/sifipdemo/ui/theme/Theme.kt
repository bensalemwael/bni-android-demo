package com.bni.sifipdemo.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val BniLightColorScheme = lightColorScheme(
    primary = BniTeal,
    onPrimary = Color.White,
    primaryContainer = BniTealLight,
    onPrimaryContainer = BniTealDeep,
    secondary = BniCoral,
    onSecondary = Color.White,
    secondaryContainer = BniTealTint,
    onSecondaryContainer = BniTealDeep,
    tertiary = BniCoralDark,
    onTertiary = Color.White,
    background = BniBackground,
    onBackground = BniText,
    surface = Color.White,
    onSurface = BniText,
    surfaceVariant = BniTealTint,
    onSurfaceVariant = BniTealDeep,
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
