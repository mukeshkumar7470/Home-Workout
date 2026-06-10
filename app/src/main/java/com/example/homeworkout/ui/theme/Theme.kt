package com.example.homeworkout.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val WorkoutDarkColorScheme = darkColorScheme(
    primary = WorkoutOrange,
    onPrimary = Color.White,
    primaryContainer = WorkoutOrangeDark,
    onPrimaryContainer = Color.White,
    secondary = WorkoutTeal,
    onSecondary = Color.White,
    tertiary = WorkoutPurple,
    background = DarkBackground,
    onBackground = DarkOnSurface,
    surface = DarkSurface,
    onSurface = DarkOnSurface,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = DarkOnSurfaceMuted,
    outline = Color(0xFF3D3D4A),
)

private val WorkoutLightColorScheme = lightColorScheme(
    primary = WorkoutOrange,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFFFE0B2),
    onPrimaryContainer = WorkoutOrangeDark,
    secondary = WorkoutTeal,
    onSecondary = Color.White,
    tertiary = WorkoutPurple,
    background = LightBackground,
    onBackground = LightOnSurface,
    surface = LightSurface,
    onSurface = LightOnSurface,
    surfaceVariant = LightSurfaceVariant,
    onSurfaceVariant = LightOnSurfaceMuted,
    outline = Color(0xFFD0D0D8),
)

@Composable
fun HomeWorkoutTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) WorkoutDarkColorScheme else WorkoutLightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content,
    )
}
