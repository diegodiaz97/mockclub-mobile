package com.diego.futty.home.view

import androidx.compose.ui.graphics.vector.ImageVector
import compose.icons.Octicons
import compose.icons.TablerIcons
import compose.icons.octicons.Home24
import compose.icons.octicons.HomeFill24
import compose.icons.tablericons.Map
import compose.icons.tablericons.Search

sealed class BottomNavScreen(
    val route: HomeRoute,
    val icon: ImageVector,
    val selectedIcon: ImageVector
) {
    object Home : BottomNavScreen(HomeRoute.Feed, Octicons.Home24, Octicons.HomeFill24)
    object Design : BottomNavScreen(HomeRoute.Design, TablerIcons.Search, TablerIcons.Search)
    object Maps : BottomNavScreen(HomeRoute.Match, TablerIcons.Map, TablerIcons.Map)

    companion object {
        val allScreens = listOf(Home, Design, Maps)
    }
}
