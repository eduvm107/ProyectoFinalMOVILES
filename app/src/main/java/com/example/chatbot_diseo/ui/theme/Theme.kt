package com.example.chatbot_diseo.ui.theme

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

private val DarkColorScheme = darkColorScheme(
    primary = TcsBlue,
    secondary = TcsBlueMid,
    tertiary = TcsBlueDark,
    background = TcsTextDark,
    surface = Color(0xFF1E1E1E),
    onPrimary = TcsWhite,
    onSecondary = TcsWhite,
    onTertiary = TcsWhite,
    onBackground = TcsWhite,
    onSurface = TcsWhite,
    surfaceVariant = Color(0xFF2D2D2D),
    onSurfaceVariant = TcsGrayText
)

private val LightColorScheme = lightColorScheme(
    primary = TcsBlue,
    secondary = TcsBlueMid,
    tertiary = TcsBlueDark,
    background = TcsGrayBackground,
    surface = TcsWhite,
    onPrimary = TcsWhite,
    onSecondary = TcsWhite,
    onTertiary = TcsWhite,
    onBackground = TcsTextDark,
    onSurface = TcsTextDark,
    surfaceVariant = TcsGraySoft,
    onSurfaceVariant = TcsTextLight
)

@Composable
fun ChatBot_DiseñoTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}