package com.batodev.arrows

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.batodev.arrows.ui.theme.LocalThemeColors
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.takeWhile
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun SplashScreen(
    isInitialized: StateFlow<Boolean>,
    onSplashComplete: () -> Unit
) {
    val themeColors = LocalThemeColors.current

    LaunchedEffect(Unit) {
        delay(GameConstants.SPLASH_SCREEN_MIN_DURATION_MS.milliseconds)

        withTimeoutOrNull(GameConstants.SPLASH_SCREEN_INIT_TIMEOUT_MS.milliseconds) {
            isInitialized
                .takeWhile { !it }
                .collect { }
        }

        delay(GameConstants.SPLASH_SCREEN_BUFFER_AFTER_INIT_MS.milliseconds)
        onSplashComplete()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(themeColors.background),
        contentAlignment = Alignment.Center
    ) {
        TriangleIcon(
            modifier = Modifier.size(80.dp),
            color = Color.White
        )
    }
}
