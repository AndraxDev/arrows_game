package com.batodev.arrows.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.batodev.arrows.SettingsScreen
import com.batodev.arrows.data.AndroidResourceBoardShapeProvider
import com.batodev.arrows.ui.AppViewModel
import com.bumble.appyx.core.modality.BuildContext
import com.bumble.appyx.core.node.Node
import org.koin.core.component.KoinComponent

class SettingsNode(
    buildContext: BuildContext,
    private val appViewModel: AppViewModel,
    private val onNavigateHome: () -> Unit,
    private val onNavigateToGenerate: () -> Unit
) : Node(buildContext), KoinComponent {
    @Composable
    override fun View(modifier: Modifier) {
        val context = LocalContext.current
        appViewModel.shapeProvider = AndroidResourceBoardShapeProvider(context)
        SettingsScreen(
            viewModel = appViewModel,
            onNavigateHome = onNavigateHome,
            onNavigateToGenerate = onNavigateToGenerate
        )
    }
}
