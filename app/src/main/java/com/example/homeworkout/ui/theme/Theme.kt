package com.example.homeworkout.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
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

@Composable
fun HomeWorkoutTheme(
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = WorkoutDarkColorScheme,
        typography = Typography,
        content = content,
    )
}
