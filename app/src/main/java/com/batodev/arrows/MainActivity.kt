package com.batodev.arrows

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.batodev.arrows.navigation.RootNode
import com.batodev.arrows.ui.AppViewModel
import com.batodev.arrows.ui.theme.ArrowsTheme
import com.bumble.appyx.core.integration.NodeHost
import com.bumble.appyx.core.integrationpoint.ActivityIntegrationPoint
import com.bumble.appyx.core.integrationpoint.IntegrationPointProvider
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.java.KoinJavaComponent.inject

class MainActivity : ComponentActivity(), IntegrationPointProvider {

    override lateinit var appyxV1IntegrationPoint: ActivityIntegrationPoint
        private set

    private val appViewModel: AppViewModel by viewModel()
    private val initializationManager: InitializationManager by inject(InitializationManager::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appyxV1IntegrationPoint = ActivityIntegrationPoint(this, savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val currentTheme by appViewModel.theme.collectAsState()
            var showSplash by remember { mutableStateOf(true) }

            LaunchedEffect(currentTheme) {
                initializationManager.markInitialized()
            }

            ArrowsTheme(themeName = currentTheme, context = this@MainActivity) {
                if (showSplash) {
                    SplashScreen(
                        isInitialized = initializationManager.isInitialized,
                        onSplashComplete = { showSplash = false }
                    )
                } else {
                    NodeHost(integrationPoint = appyxV1IntegrationPoint) { buildContext ->
                        RootNode(
                            buildContext = buildContext,
                            appViewModel = appViewModel
                        )
                    }
                }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        appyxV1IntegrationPoint.onSaveInstanceState(outState)
    }
}
