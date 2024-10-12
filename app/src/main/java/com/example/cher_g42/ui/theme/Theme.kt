package com.example.cher_g42.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = Green,
    secondary = White,
    tertiary = LightGreen,
    background = White,
    surface = Green,
    onPrimary = Color.White,
    onSecondary = Green,
    onBackground = Color.Black,
    onSurface = Color.White,
)

private val LightColorScheme = lightColorScheme(
    primary = Green,
    secondary = Color.Black,
    tertiary = LightGreen,
    background = White,
    surface = Green,
    onPrimary = Color.White,
    onSecondary = Green,
    onBackground = Color.Black,
    onSurface = Color.White,
)

@Composable
fun CheRg42Theme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}