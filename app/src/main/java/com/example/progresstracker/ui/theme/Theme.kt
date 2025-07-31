package com.example.progresstracker.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val LightColors = lightColorScheme(
    primary = GrayPrimary,
    onPrimary = GrayOnPrimary,
    secondary = GraySecondary,
    onSecondary = GrayOnSecondary,
    background = GrayBackground,
    surface = GraySurface,
    onSurface = GrayOnSurface,
    error = GrayError,
    onError = GrayOnError,
)

private val DarkColors = darkColorScheme(
    primary = GrayOnSurface,
    onPrimary = Color.Black,
    secondary = GraySecondary,
    onSecondary = Color.Black,
    background = Color(0xFF121212),
    surface = Color(0xFFBDBDBD),
    onSurface = Color(0xFFE0E0E0),
    error = Color(0xFFCF6679),
    onError = Color.Black,
)


@Composable
fun ProgressTrackerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColors
        else -> LightColors
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}