package com.diego.futty.app.presentation.view

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
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
import com.diego.futty.core.presentation.theme.FuttyTheme
import com.diego.futty.core.presentation.theme.colorGrey0
import com.diego.futty.core.presentation.theme.colorGrey100
import com.diego.futty.core.presentation.theme.colorGrey500
import com.diego.futty.core.presentation.theme.colorGrey900
import com.diego.futty.core.presentation.utils.SetStatusBarColor
import com.diego.futty.core.presentation.utils.Transitions
import com.diego.futty.home.design.presentation.component.avatar.Avatar
import com.diego.futty.home.design.presentation.component.avatar.AvatarSize
import com.diego.futty.home.design.presentation.screen.DesignScreen
import com.diego.futty.home.design.presentation.viewmodel.DesignViewModel
import com.diego.futty.home.feed.presentation.screen.FeedScreen
import com.diego.futty.home.feed.presentation.viewmodel.FeedViewModel
import com.diego.futty.home.match.presentation.screen.MatchScreen
import com.diego.futty.home.match.presentation.viewmodel.MatchViewModel
import com.diego.futty.home.view.BottomNavScreen
import com.diego.futty.home.view.HomeRoute
import com.diego.futty.home.view.HomeViewModel
import com.diego.futty.setup.view.SetupView
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
@Preview
fun HomeView(navigateToLogin: () -> Unit) {
    val appViewModel = koinViewModel<HomeViewModel>()
    val designViewModel = koinViewModel<DesignViewModel>()
    val feedViewModel = koinViewModel<FeedViewModel>()
    val matchViewModel = koinViewModel<MatchViewModel>()
    val navController = rememberNavController()

    FuttyTheme(appViewModel.palette.value) {
        SetStatusBarColor(Color.Transparent)
        LaunchedEffect(true) {
            appViewModel.setup()
            designViewModel.setup(navController)
            feedViewModel.setup(navController)
            // matchViewModel.setup()
        }

        Column {
            NavHost(
                modifier = Modifier.weight(1f),
                navController = navController,
                startDestination = HomeRoute.HomeGraph
            ) {
                navigation<HomeRoute.HomeGraph>(
                    startDestination = appViewModel.currentRoute.value
                ) {
                    composable<HomeRoute.Feed>(
                        enterTransition = { EnterTransition.None }, // Sin animación de entrada
                        exitTransition = { ExitTransition.None },    // Sin animación de salida
                    ) {
                        LaunchedEffect(true) {
                            appViewModel.updateRoute(HomeRoute.Feed)
                        }
                        FeedScreen(viewModel = feedViewModel)
                    }

                    composable<HomeRoute.Design>(
                        enterTransition = { EnterTransition.None }, // Sin animación de entrada
                        exitTransition = { ExitTransition.None },    // Sin animación de salida
                    ) {
                        LaunchedEffect(true) {
                            appViewModel.updateRoute(HomeRoute.Design)
                        }
                        DesignScreen(viewModel = designViewModel)
                    }

                    composable<HomeRoute.Match>(
                        enterTransition = { EnterTransition.None }, // Sin animación de entrada
                        exitTransition = { ExitTransition.None },    // Sin animación de salida
                    ) {
                        LaunchedEffect(true) {
                            appViewModel.updateRoute(HomeRoute.Match)
                        }
                        MatchScreen(viewModel = matchViewModel) { }
                    }

                    composable<HomeRoute.Setup>(
                        enterTransition = Transitions.RightScreenEnter,
                        exitTransition = Transitions.LeftScreenExit,
                        popEnterTransition = Transitions.RightScreenPopEnter,
                        popExitTransition = Transitions.LeftScreenPopExit
                    ) {
                        LaunchedEffect(true) {
                            appViewModel.updateRoute(HomeRoute.Setup)
                        }
                        SetupView(
                            onBack = { navController.popBackStack() },
                            navigateToLogin = navigateToLogin,
                        )
                    }
                }
            }
            if (appViewModel.showBottomBar.value) {
                BottomNavBar(navController, appViewModel.currentRoute.value)
            }
        }
    }
}

private fun navigateTo(navController: NavController, current: HomeRoute, destination: HomeRoute) {
    if (current != destination) {
        navController.navigate(destination) {
            popUpTo(current) { inclusive = true }
        }
    }
}

@Composable
fun BottomNavBar(navController: NavController, currentRoute: HomeRoute) {
    Column {
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth().height(1.dp),
            color = colorGrey100(),
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(colorGrey0())
                .padding(top = 4.dp, bottom = 20.dp),
            horizontalArrangement = Arrangement.SpaceAround,
        ) {
            BottomNavScreen.allScreens.forEach { screen ->
                Avatar.IconAvatar(
                    icon = screen.icon,
                    tint = if (currentRoute == screen.route) colorGrey900() else colorGrey500(),
                    background = Color.Transparent,
                    avatarSize = AvatarSize.Big,
                ) {
                    navigateTo(navController, currentRoute, screen.route)
                }.Draw()
            }
        }
    }
}
