package com.diego.futty.home.view

import androidx.compose.ui.graphics.vector.ImageVector
import compose.icons.TablerIcons
import compose.icons.tablericons.Home
import compose.icons.tablericons.Map
import compose.icons.tablericons.Paint

sealed class BottomNavScreen(val route: HomeRoute, val icon: ImageVector, val label: String) {
    object Home : BottomNavScreen(HomeRoute.Feed, TablerIcons.Home, "Inicio")
    object Design : BottomNavScreen(HomeRoute.Design, TablerIcons.Paint, "Diseño")
    object Maps : BottomNavScreen(HomeRoute.Match, TablerIcons.Map, "Mapa")

    companion object {
        val allScreens = listOf(Home, Design, Maps)
    }
}
