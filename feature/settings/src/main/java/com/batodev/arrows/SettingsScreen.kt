package com.batodev.arrows

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import dev.andrax.arrows.feature.settings.BuildConfig
import com.batodev.arrows.ui.AnimationSpeedSelectionDialog
import com.batodev.arrows.ui.AppNavigationBar
import com.batodev.arrows.ui.AppViewModel
import com.batodev.arrows.ui.DebugMenu
import com.batodev.arrows.ui.FeedbackSection
import com.batodev.arrows.ui.LegalSection
import com.batodev.arrows.ui.NavigationDestination
import com.batodev.arrows.ui.PreferencesParams
import com.batodev.arrows.ui.PreferencesSection
import com.batodev.arrows.ui.ThemeSelectionDialog
import com.batodev.arrows.ui.ThirdPartyLicensesDialog
import com.batodev.arrows.ui.theme.LocalThemeColors

@Composable
fun SettingsScreen(
    viewModel: AppViewModel,
    onNavigateHome: () -> Unit = {},
    onNavigateToGenerate: () -> Unit = {}
) {
    val context = LocalContext.current
    val levelNumber by viewModel.levelNumber.collectAsState()
    val themeColors = LocalThemeColors.current
    var showThemeDialog by remember { mutableStateOf(false) }
    var showSpeedDialog by remember { mutableStateOf(false) }
    var showLicensesDialog by remember { mutableStateOf(false) }
    val currentTheme by viewModel.theme.collectAsState()
    val currentSpeed by viewModel.animationSpeed.collectAsState()

    SettingsDialogs(
        SettingsDialogsState(
            showThemeDialog, { showThemeDialog = it }, currentTheme, { viewModel.saveTheme(it) },
            showSpeedDialog, { showSpeedDialog = it }, currentSpeed, { viewModel.saveAnimationSpeed(it) }
        )
    )
    if (showLicensesDialog) {
        ThirdPartyLicensesDialog(onDismiss = { showLicensesDialog = false })
    }

    SettingsScaffold(
        SettingsScaffoldParams(
            viewModel, context, themeColors,
            levelNumber, currentTheme, currentSpeed,
            { showThemeDialog = true }, { showSpeedDialog = true }, { showLicensesDialog = true },
            onNavigateHome, onNavigateToGenerate
        )
    )
}

private data class SettingsScaffoldParams(
    val viewModel: AppViewModel,
    val context: android.content.Context,
    val themeColors: com.batodev.arrows.ui.theme.ThemeColors,
    val levelNumber: Int,
    val currentTheme: String,
    val currentSpeed: String,
    val onThemeClick: () -> Unit,
    val onSpeedClick: () -> Unit,
    val onLicensesClick: () -> Unit,
    val onNavigateHome: () -> Unit,
    val onNavigateToGenerate: () -> Unit
)

@Composable
private fun Modifier.settingsEntryModifier(visible: Boolean, sectionIndex: Int): Modifier {
    val translationX by animateFloatAsState(
        targetValue = if (visible) 0f else GameConstants.SETTINGS_ENTER_OFFSET_DP,
        animationSpec = tween(
            durationMillis = GameConstants.SETTINGS_ENTER_ANIM_DURATION,
            delayMillis = SettingsScreenLogic.sectionEntryDelayMs(sectionIndex)
        ),
        label = "settingsEntryTransX$sectionIndex"
    )
    val alpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(
            durationMillis = GameConstants.SETTINGS_ENTER_ANIM_DURATION,
            delayMillis = SettingsScreenLogic.sectionEntryDelayMs(sectionIndex)
        ),
        label = "settingsEntryAlpha$sectionIndex"
    )
    return graphicsLayer {
        this.translationX = translationX * density
        this.alpha = alpha
    }
}

@Composable
private fun SettingsScaffold(params: SettingsScaffoldParams) {
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { visible = true }

    Scaffold(
        containerColor = params.themeColors.background,
        bottomBar = {
            Column(modifier = Modifier.fillMaxWidth()) {
                AppNavigationBar(
                    selectedDestination = NavigationDestination.SETTINGS,
                    levelNumber = params.levelNumber,
                    themeColors = params.themeColors,
                    onNavigateHome = params.onNavigateHome,
                    onNavigateToGenerate = params.onNavigateToGenerate,
                    onNavigateToSettings = {}
                )
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(modifier = Modifier.settingsEntryModifier(visible, sectionIndex = 0)) {
                PreferencesSection(
                    PreferencesParams(
                        params.viewModel, params.themeColors, params.currentTheme, params.currentSpeed,
                        params.onThemeClick, params.onSpeedClick
                    )
                )
            }
            Box(modifier = Modifier.settingsEntryModifier(visible, sectionIndex = 1)) {
                FeedbackSection(params.context, params.themeColors)
            }
            Box(modifier = Modifier.settingsEntryModifier(visible, sectionIndex = 3)) {
                LegalSection(
                    params.context,
                    params.themeColors,
                    params.onLicensesClick,
                )
            }
            if (BuildConfig.DRAW_DEBUG_STUFF) DebugMenu(params.viewModel)
        }
    }
}

private data class SettingsDialogsState(
    val showThemeDialog: Boolean,
    val onShowThemeDialog: (Boolean) -> Unit,
    val currentTheme: String,
    val onThemeSelected: (String) -> Unit,
    val showSpeedDialog: Boolean,
    val onShowSpeedDialog: (Boolean) -> Unit,
    val currentSpeed: String,
    val onSpeedSelected: (String) -> Unit
)

@Composable
private fun SettingsDialogs(state: SettingsDialogsState) {
    if (state.showThemeDialog) {
        ThemeSelectionDialog(state.currentTheme, { state.onShowThemeDialog(false) }) {
            state.onThemeSelected(it)
            state.onShowThemeDialog(false)
        }
    }
    if (state.showSpeedDialog) {
        AnimationSpeedSelectionDialog(state.currentSpeed, { state.onShowSpeedDialog(false) }) {
            state.onSpeedSelected(it)
            state.onShowSpeedDialog(false)
        }
    }
}
