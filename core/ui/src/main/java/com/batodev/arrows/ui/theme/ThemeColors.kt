package com.batodev.arrows.ui.theme

import android.content.Context
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

@Immutable
data class ThemeColors(
    val background: Color,
    val accent: Color,
    val snake: Color,
    val topBarButton: Color,
    val bottomBar: Color
)

val LocalThemeColors = staticCompositionLocalOf {
    ThemeColors(
        background = BlueBackground,
        accent = AccentBlue,
        snake = SnakeBlue,
        topBarButton = AccentBlue,
        bottomBar = BottomBarBackground
    )
}

private val DarkColorScheme = darkColorScheme(primary = Purple80, secondary = PurpleGrey80, tertiary = Pink80)
private val LightColorScheme = lightColorScheme(primary = Purple40, secondary = PurpleGrey40, tertiary = Pink40)

private fun getThemeColors(themeName: String, context: Context): ThemeColors = when (themeName) {
    "Green" -> ThemeColors(
        GreenBackground, GreenAccent, GreenSnake,
        GreenAccent.copy(alpha = 0.2f), GreenBackground.copy(alpha = 0.8f)
    )
    "Red" -> ThemeColors(
        RedBackground, RedAccent, RedSnake,
        RedAccent.copy(alpha = 0.2f), RedBackground.copy(alpha = 0.8f)
    )
    "Yellow" -> ThemeColors(
        YellowBackground, YellowAccent, YellowSnake,
        YellowAccent.copy(alpha = 0.2f), YellowBackground.copy(alpha = 0.8f)
    )
    "Orange" -> ThemeColors(
        OrangeBackground, OrangeAccent, OrangeSnake,
        OrangeAccent.copy(alpha = 0.2f), OrangeBackground.copy(alpha = 0.8f)
    )
    "Black and White" -> ThemeColors(
        BWBackground, BWAccent, BWSnake,
        BWAccent.copy(alpha = 0.1f), BWBackground.copy(alpha = 0.8f)
    )
    "Dark Room (red)" -> ThemeColors(
        AMOLED_DARK_ROOM_BACKGROUND, AMOLED_DARK_ROOM_RED, AMOLED_DARK_ROOM_RED_SNAKE,
        AMOLED_DARK_ROOM_RED.copy(alpha = 0.1f), AMOLED_DARK_ROOM_BACKGROUND.copy(alpha = 0.8f)
    )
    "Blue" -> ThemeColors(
        BlueBackground, AccentBlue, SnakeBlue,
        AccentBlue.copy(alpha = 0.2f), BlueBackground.copy(alpha = 0.8f)
    )
    "Purple" -> ThemeColors(
        PurpleBackground, PurpleAccent, PurpleSnake,
        PurpleAccent.copy(alpha = 0.2f), PurpleBackground.copy(alpha = 0.8f)
    )
    else -> {
        val dynamicSystemColorBackground = dynamicDarkColorScheme(context).background
        val dynamicSystemColorAccent = dynamicDarkColorScheme(context).primary
        return ThemeColors(
            dynamicSystemColorBackground, dynamicSystemColorAccent, dynamicSystemColorAccent,
            dynamicSystemColorAccent.copy(alpha = 0.2f), dynamicSystemColorBackground.copy(alpha = 0.8f)
        )
    }
}

@Composable
fun ArrowsTheme(
    themeName: String = "Dark",
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    context: Context,
    content: @Composable () -> Unit
) {
    val themeColors = getThemeColors(themeName, context)
    val colorScheme = when {
        dynamicColor && themeName == "Dark" -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme || themeName != "Light" -> DarkColorScheme.copy(
            background = themeColors.background,
            surface = themeColors.background,
            primary = themeColors.accent
        )
        else -> LightColorScheme
    }
    CompositionLocalProvider(LocalThemeColors provides themeColors) {
        MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
    }
}
