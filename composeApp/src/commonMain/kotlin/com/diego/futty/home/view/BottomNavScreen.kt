package com.diego.futty.home.view

import androidx.compose.ui.graphics.vector.ImageVector
import compose.icons.TablerIcons
import compose.icons.tablericons.Diamond
import compose.icons.tablericons.Home
import compose.icons.tablericons.Search

sealed class BottomNavScreen(
    val text: String,
    val route: HomeRoute,
    val icon: ImageVector,
    val selectedIcon: ImageVector
) {
    object Home : BottomNavScreen("Inicio", HomeRoute.Feed, TablerIcons.Home, TablerIcons.Home)
    object Discover : BottomNavScreen("Explorar", HomeRoute.Match, TablerIcons.Diamond, TablerIcons.Diamond)
    object Search : BottomNavScreen("Buscar", HomeRoute.Design, TablerIcons.Search, TablerIcons.Search)

    companion object {
        val allScreens = listOf(Home, Discover, Search)
    }
}
