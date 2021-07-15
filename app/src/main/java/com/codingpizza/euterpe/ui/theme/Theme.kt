package com.codingpizza.euterpe.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

private val DarkColorPalette = darkColors(
    primary = primaryColor,
    primaryVariant = primaryVariantColor,
    secondary = secondaryColor,
    secondaryVariant = secondaryVariantColor,
    surface = surfaceColor,
    onSurface = onSurfaceColor,
    background = backgroundColor,
    onBackground = onBackgroundColor
)

private val LightColorPalette = lightColors(
    primary = primaryColor,
    primaryVariant = primaryVariantColor,
    secondary = secondaryColor,
    secondaryVariant = secondaryVariantColor,
    surface = surfaceColor,
    onSurface = onSurfaceColor,
    background = backgroundColor,
    onBackground = onBackgroundColor
)

@Composable
fun EuterpeTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable() () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
