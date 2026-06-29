package com.batodev.arrows.ui

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.batodev.arrows.GameConstants
import dev.andrax.arrows.core.resources.R
import com.batodev.arrows.ui.theme.ThemeColors
import com.batodev.arrows.ui.theme.White

@Composable
fun AppNavigationBar(
    selectedDestination: NavigationDestination,
    levelNumber: Int,
    themeColors: ThemeColors,
    onNavigateHome: () -> Unit = {},
    onNavigateToGenerate: () -> Unit = {},
    onNavigateToSettings: () -> Unit = {}
) {
    val isGeneratorUnlocked = levelNumber >= GameConstants.GENERATOR_UNLOCK_LEVEL

    NavigationBar(
        containerColor = themeColors.bottomBar,
        contentColor = White
    ) {
        GeneratorNavigationItem(
            isUnlocked = isGeneratorUnlocked,
            selected = selectedDestination == NavigationDestination.GENERATOR,
            themeColors = themeColors,
            onNavigate = onNavigateToGenerate
        )
        HomeNavigationItem(
            selected = selectedDestination == NavigationDestination.HOME,
            themeColors = themeColors,
            onNavigate = onNavigateHome
        )
        SettingsNavigationItem(
            selected = selectedDestination == NavigationDestination.SETTINGS,
            themeColors = themeColors,
            onNavigate = onNavigateToSettings
        )
    }
}

@Composable
fun RowScope.HomeNavigationItem(selected: Boolean, themeColors: ThemeColors, onNavigate: () -> Unit) {
    NavigationBarItem(
        icon = {
            Icon(
                Icons.Default.Home,
                contentDescription = stringResource(R.string.home_label)
            )
        },
        label = { Text(stringResource(R.string.home_label)) },
        selected = selected,
        onClick = {
            if (!selected) {
                onNavigate()
            }
        },
        colors = NavigationBarItemDefaults.colors(
            selectedIconColor = White,
            indicatorColor = themeColors.topBarButton,
            selectedTextColor = White,
            unselectedIconColor = themeColors.accent,
            unselectedTextColor = themeColors.accent
        )
    )
}

@Composable
fun RowScope.SettingsNavigationItem(selected: Boolean, themeColors: ThemeColors, onNavigate: () -> Unit) {
    NavigationBarItem(
        icon = {
            Icon(
                Icons.Default.Settings,
                contentDescription = stringResource(R.string.settings_label)
            )
        },
        label = { Text(stringResource(R.string.settings_label)) },
        selected = selected,
        onClick = {
            if (!selected) {
                onNavigate()
            }
        },
        colors = NavigationBarItemDefaults.colors(
            selectedIconColor = White,
            indicatorColor = themeColors.topBarButton,
            selectedTextColor = White,
            unselectedIconColor = themeColors.accent,
            unselectedTextColor = themeColors.accent
        )
    )
}

@Composable
fun RowScope.GeneratorNavigationItem(isUnlocked: Boolean, selected: Boolean = false, themeColors: ThemeColors, onNavigate: () -> Unit) {
    val icon = if (isUnlocked) Icons.Default.AutoAwesome else Icons.Default.Lock
    val label = if (isUnlocked) stringResource(R.string.custom_gen_title)
    else stringResource(R.string.level_label, GameConstants.GENERATOR_UNLOCK_LEVEL)

    NavigationBarItem(
        icon = { Icon(icon, contentDescription = stringResource(R.string.content_description_generate)) },
        label = { Text(label) },
        selected = selected,
        onClick = {
            if (isUnlocked && !selected) {
                onNavigate()
            }
        },
        colors = NavigationBarItemDefaults.colors(
            selectedIconColor = White,
            unselectedIconColor = themeColors.accent,
            selectedTextColor = White,
            unselectedTextColor = themeColors.accent,
            indicatorColor = themeColors.topBarButton
        )
    )
}
