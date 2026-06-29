package com.batodev.arrows

import android.app.Activity
import android.content.Context
import android.view.HapticFeedbackConstants
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CenterFocusWeak
import androidx.compose.material.icons.filled.Grid4x4
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.andrax.arrows.core.resources.R
import com.batodev.arrows.data.AndroidResourceBoardShapeProvider
import com.batodev.arrows.data.GameStateDao
import com.batodev.arrows.data.UserPreferencesRepository
import com.batodev.arrows.engine.GameEngine
import com.batodev.arrows.engine.GameEngineConfig
import com.batodev.arrows.engine.GameEngineFeatures
import com.batodev.arrows.engine.GameUiState
import com.batodev.arrows.ui.AppViewModel
import com.batodev.arrows.ui.theme.ThemeColors
import com.batodev.arrows.ui.theme.White
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

data class GameWonStateParams(
    val engine: GameEngine,
    val viewModel: AppViewModel,
    val activity: Activity,
    val onFinish: () -> Unit = {}
)

data class GameAreaParams(
    val engine: GameEngine,
    val uiState: GameUiState,
    val tapAnimations: SnapshotStateList<TapAnimationState>,
    val guidanceAlpha: Float,
    val showGuidanceLines: Boolean,
    val themeColors: ThemeColors,
    val activity: Activity?,
    val onToggleGuidance: () -> Unit,
    val showIntro: Boolean,
    val onDismissIntro: () -> Unit
)

data class GameScreenContentParams(
    val engine: GameEngine,
    val uiState: GameUiState,
    val activity: Activity?,
    val context: Context,
    val tapAnimations: SnapshotStateList<TapAnimationState>,
    val guidanceAlpha: Float,
    val showGuidanceLines: Boolean,
    val themeColors: ThemeColors,
    val handleHint: () -> Unit,
    val onToggleGuidance: () -> Unit,
    val showCelebrationVideo: Boolean,
    val onCelebrationComplete: () -> Unit,
    val showIntro: Boolean,
    val onDismissIntro: () -> Unit,
    val onBack: () -> Unit = {}
)

data class HintHandlerParams(
    val engine: GameEngine,
    val activity: Activity?
)

fun buildHintHandler(params: HintHandlerParams): () -> Unit = {
    params.engine.showHint()
}

fun createGameEngineFactory(
    view: android.view.View,
    context: Context,
    repository: UserPreferencesRepository,
    gameStateDao: GameStateDao,
    customParams: CustomGameParams
): GameEngine.Factory {
    return GameEngine.Factory(
        config = GameEngineConfig(
            repository = repository,
            gameStateDao = gameStateDao,
            isCustomGame = customParams.isCustom
        ),
        features = GameEngineFeatures(
            onVibrate = { view.performHapticFeedback(HapticFeedbackConstants.CLOCK_TICK) },
            soundManager = SoundManager(context),
            shapeProvider = AndroidResourceBoardShapeProvider(context),
            forcedWidth = customParams.customWidth,
            forcedHeight = customParams.customHeight,
            forcedShape = customParams.customShape
        )
    )
}

@Composable
fun BoxScope.GuidanceToggleButton(
    showGuidanceLines: Boolean,
    themeColors: ThemeColors,
    onToggleGuidance: () -> Unit
) {
    IconButton(
        onClick = onToggleGuidance,
        modifier = Modifier.align(Alignment.BottomEnd).padding(8.dp).size(48.dp),
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = if (showGuidanceLines) themeColors.accent else themeColors.topBarButton,
            contentColor = White
        )
    ) {
        Icon(
            imageVector = Icons.Default.Grid4x4,
            contentDescription = stringResource(R.string.content_description_guidance_lines),
            tint = White
        )
    }
}

@Composable
fun BoxScope.ResetViewButton(
    themeColors: ThemeColors,
    onResetView: () -> Unit
) {
    IconButton(
        onClick = onResetView,
        modifier = Modifier.align(Alignment.BottomStart).padding(8.dp).size(48.dp),
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = themeColors.topBarButton,
            contentColor = White
        )
    ) {
        Icon(
            imageVector = Icons.Default.CenterFocusWeak,
            contentDescription = stringResource(R.string.content_description_reset_view),
            tint = White
        )
    }
}

fun shouldShowInterstitialAd(isAdFree: Boolean, gamesCompleted: Int): Boolean {
    return !isAdFree && gamesCompleted > 0 && gamesCompleted % GameConstants.GAMES_BETWEEN_INTERSTITIALS == 0
}

suspend fun finishGameAfterCelebration(params: GameWonStateParams, waitForConfetti: Boolean) {
    if (waitForConfetti) {
        delay(GameConstants.GAME_WON_EXIT_DELAY.milliseconds)
    }
    params.viewModel.incrementGamesCompleted()
    params.onFinish()
}
