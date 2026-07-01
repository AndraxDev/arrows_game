package com.batodev.arrows

import androidx.compose.animation.core.tween
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

@Composable
fun SplashScreen(
    onSplashComplete: () -> Unit,
    splashDurationMs: Long = 300
) {
    val themeColors = LocalThemeColors.current

    LaunchedEffect(Unit) {
        delay(splashDurationMs)
        onSplashComplete()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(themeColors.background),
        contentAlignment = Alignment.Center
    ) {
        com.batodev.arrows.TriangleIcon(
            modifier = Modifier.size(80.dp),
            color = Color.White
        )
    }
}
