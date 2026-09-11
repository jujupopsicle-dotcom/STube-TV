package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val YouTubeTvColorScheme =
  darkColorScheme(
    primary = YouTubeRed,
    onPrimary = YouTubeTextPrimary,
    primaryContainer = YouTubeRedDark,
    onPrimaryContainer = YouTubeTextPrimary,
    secondary = YouTubeBlue,
    onSecondary = YouTubeBlack,
    background = YouTubeBlack,
    onBackground = YouTubeTextPrimary,
    surface = YouTubeSurface,
    onSurface = YouTubeTextPrimary,
    surfaceVariant = YouTubeSurfaceVariant,
    onSurfaceVariant = YouTubeTextSecondary
  )

@Composable
fun MyApplicationTheme(
  content: @Composable () -> Unit,
) {
  MaterialTheme(
    colorScheme = YouTubeTvColorScheme,
    typography = Typography,
    content = content
  )
}

