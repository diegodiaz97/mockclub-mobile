package com.diego.futty.home.view

import androidx.compose.ui.graphics.vector.ImageVector
import com.adamglin.PhosphorIcons
import com.adamglin.phosphoricons.Bold
import com.adamglin.phosphoricons.Fill
import com.adamglin.phosphoricons.bold.Bell
import com.adamglin.phosphoricons.bold.House
import com.adamglin.phosphoricons.bold.MagnifyingGlass
import com.adamglin.phosphoricons.bold.TShirt
import com.adamglin.phosphoricons.fill.Bell
import com.adamglin.phosphoricons.fill.House
import com.adamglin.phosphoricons.fill.MagnifyingGlass
import com.adamglin.phosphoricons.fill.TShirt

sealed class BottomNavScreen(
    val text: String,
    val route: HomeRoute,
    val icon: ImageVector,
    val selectedIcon: ImageVector
) {
    data object Home : BottomNavScreen(
        "Inicio",
        HomeRoute.Feed,
        PhosphorIcons.Bold.House,
        PhosphorIcons.Fill.House
    )

    data object Challenges : BottomNavScreen(
        "Retos",
        HomeRoute.Challenge,
        PhosphorIcons.Bold.TShirt,
        PhosphorIcons.Fill.TShirt
    )

    data object Search : BottomNavScreen(
        "Buscar",
        HomeRoute.Design,
        PhosphorIcons.Bold.MagnifyingGlass,
        PhosphorIcons.Fill.MagnifyingGlass
    )

    data object Notifications : BottomNavScreen(
        "Notificationes",
        HomeRoute.Match,
        PhosphorIcons.Bold.Bell,
        PhosphorIcons.Fill.Bell
    )

    companion object {
        val allScreens = listOf(Home, Challenges, Search, Notifications)
    }
}
