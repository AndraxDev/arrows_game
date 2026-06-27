package com.batodev.arrows.ui.game

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.batodev.arrows.GameConstants
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun WinCelebrationScreen(onCelebrationComplete: () -> Unit) {
    var videoAlpha by remember { mutableFloatStateOf(0f) }
    var shouldShowCelebration by remember { mutableStateOf(true) }
    val celebration = remember { CelebrationContentSelector.selectContent() }

    val animatedAlpha by animateFloatAsState(
        targetValue = videoAlpha,
        animationSpec = tween(durationMillis = GameConstants.VIDEO_FADE_IN_DURATION),
        label = "video_fade"
    )

    PlayCelebrationTimeline(
        onFadeInStart = { videoAlpha = 1f },
        onFadeOutStart = { videoAlpha = 0f },
        onComplete = {
            shouldShowCelebration = false
            onCelebrationComplete()
        }
    )

    if (shouldShowCelebration) {
        CelebrationContent(celebration.labelResId, animatedAlpha)
    }
}

@Composable
private fun PlayCelebrationTimeline(
    onFadeInStart: () -> Unit,
    onFadeOutStart: () -> Unit,
    onComplete: () -> Unit,
) {
    LaunchedEffect(Unit) {
        delay(GameConstants.VIDEO_PREPARATION_DELAY.milliseconds)
        onFadeInStart()
        delay(GameConstants.VIDEO_FADE_IN_DURATION.toLong().milliseconds)
        delay(GameConstants.VIDEO_DISPLAY_DURATION.toLong().milliseconds)
        onFadeOutStart()
        delay(GameConstants.VIDEO_FADE_OUT_DURATION.toLong().milliseconds)
        onComplete()
    }
}

@Composable
private fun CelebrationContent(
    labelResId: Int,
    alpha: Float,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        // Black overlay fades out (1-alpha) for fade-in, fades in for fade-out
        Box(
            modifier = Modifier
                .fillMaxSize()
                .alpha(1f - alpha)
                .background(Color.Black)
        )
        // Text fades with the content
        Text(
            text = stringResource(labelResId),
            fontSize = GameConstants.CONGRATULATIONS_FONT_SIZE.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.alpha(alpha)
        )
    }
}
