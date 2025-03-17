package com.diego.futty.app

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.diego.futty.app.presentation.viewmodel.AppViewModel
import com.diego.futty.core.presentation.theme.FuttyTheme
import com.diego.futty.core.presentation.theme.colorGrey0
import com.diego.futty.core.presentation.theme.colorGrey200
import com.diego.futty.core.presentation.theme.colorGrey900
import com.diego.futty.design.presentation.component.avatar.Avatar
import com.diego.futty.design.presentation.component.avatar.AvatarSize
import com.diego.futty.design.presentation.screen.DesignScreen
import com.diego.futty.design.presentation.viewmodel.DesignViewModel
import com.diego.futty.design.utils.SetStatusBarColor
import com.diego.futty.design.utils.Transitions
import com.diego.futty.match.presentation.screen.MatchScreen
import com.diego.futty.match.presentation.viewmodel.MatchViewModel
import compose.icons.TablerIcons
import compose.icons.tablericons.BallFootball
import compose.icons.tablericons.BrandPinterest
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
@Preview
fun App() {
    val appViewModel = koinViewModel<AppViewModel>()
    val designViewModel = koinViewModel<DesignViewModel>()
    val matchViewModel = koinViewModel<MatchViewModel>()
    val navController = rememberNavController()

    FuttyTheme(designViewModel.palette.value) {
        SetStatusBarColor(Color.Transparent)
        LaunchedEffect(true) {
            appViewModel.setup()
            designViewModel.setup()
            matchViewModel.setup()
        }

        Column {
            NavHost(
                modifier = Modifier.weight(1f),
                navController = navController,
                startDestination = AppRoute.Graph
            ) {
                navigation<AppRoute.Graph>(
                    startDestination = appViewModel.currentRoute.value
                ) {
                    composable<AppRoute.Design>(
                        enterTransition = Transitions.LeftScreenEnter,
                        exitTransition = Transitions.LeftScreenExit,
                        popEnterTransition = Transitions.LeftScreenPopEnter,
                        popExitTransition = Transitions.LeftScreenPopExit
                    ) {
                        LaunchedEffect(true) {
                            appViewModel.updateRoute(AppRoute.Design)
                        }
                        DesignScreen(viewModel = designViewModel)
                    }

                    composable<AppRoute.Match>(
                        enterTransition = Transitions.RightScreenEnter,
                        exitTransition = Transitions.RightScreenExit,
                        popEnterTransition = Transitions.RightScreenPopEnter,
                        popExitTransition = Transitions.RightScreenPopExit
                    ) {
                        LaunchedEffect(true) {
                            appViewModel.updateRoute(AppRoute.Match)
                        }
                        MatchScreen(viewModel = matchViewModel) { }
                    }
                }
            }
            BottomNavBar(navController, appViewModel.currentRoute.value)
        }
    }
}

private fun navigateTo(navController: NavController, current: AppRoute, destination: AppRoute) {
    if (current != destination) {
        navController.navigate(destination)
    }
}

@Composable
fun BottomNavBar(navController: NavController, currentRoute: AppRoute) {
    Column {
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth().height(1.dp),
            color = colorGrey200(),
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(colorGrey0())
                .padding(top = 8.dp, bottom = 24.dp),
            horizontalArrangement = Arrangement.SpaceAround,
        ) {
            Avatar.IconAvatar(
                icon = TablerIcons.BrandPinterest,
                tint = colorGrey900(),
                background = Color.Transparent,
                avatarSize = AvatarSize.Big,
            ) {
                navigateTo(navController, currentRoute, AppRoute.Design)
            }.Draw()

            Avatar.IconAvatar(
                icon = TablerIcons.BallFootball,
                tint = colorGrey900(),
                background = Color.Transparent,
                avatarSize = AvatarSize.Big,
            ) {
                navigateTo(navController, currentRoute, AppRoute.Match)
            }.Draw()
        }
    }
}
