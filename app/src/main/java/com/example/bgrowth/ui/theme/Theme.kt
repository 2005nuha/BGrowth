package com.example.bgrowth.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = BGrowthAccent,
    secondary = BGrowthSecondary,
    tertiary = BGrowthAccent,
    background = BGrowthDark,
    surface = BGrowthPrimary,
    onPrimary = BGrowthDark,
    onSecondary = BGrowthSurface,
    onBackground = BGrowthSurface,
    onSurface = BGrowthSurface,
    error = BGrowthError
)

private val LightColorScheme = lightColorScheme(
    primary = BGrowthPrimary,
    secondary = BGrowthSecondary,
    tertiary = BGrowthAccent,
    background = BGrowthBackground,
    surface = BGrowthSurface,
    surfaceVariant = BGrowthSoftSurface,
    outline = BGrowthBorder,
    onPrimary = BGrowthSurface,
    onSecondary = BGrowthSurface,
    onBackground = BGrowthDark,
    onSurface = BGrowthDark,
    onSurfaceVariant = BGrowthSecondaryText,
    error = BGrowthError
)

@Composable
fun BGrothTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
