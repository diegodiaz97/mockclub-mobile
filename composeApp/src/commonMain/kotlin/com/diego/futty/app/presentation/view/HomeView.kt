package com.diego.futty.app.presentation.view

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
import com.diego.futty.app.presentation.viewmodel.HomeViewModel
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
import com.diego.futty.profile.presentation.screen.ProfileScreen
import com.diego.futty.profile.presentation.viewmodel.ProfileViewModel
import compose.icons.TablerIcons
import compose.icons.tablericons.BallFootball
import compose.icons.tablericons.BrandPinterest
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
@Preview
fun HomeView() {
    val appViewModel = koinViewModel<HomeViewModel>()
    val designViewModel = koinViewModel<DesignViewModel>()
    val matchViewModel = koinViewModel<MatchViewModel>()
    val profileViewModel = koinViewModel<ProfileViewModel>()
    val navController = rememberNavController()

    FuttyTheme(profileViewModel.palette.value) {
        SetStatusBarColor(Color.Transparent)
        LaunchedEffect(true) {
            appViewModel.setup()
            designViewModel.setup(navController)
            matchViewModel.setup()
            profileViewModel.setup(navController)
        }

        Column {
            NavHost(
                modifier = Modifier.weight(1f),
                navController = navController,
                startDestination = HomeRoute.Graph
            ) {
                navigation<HomeRoute.Graph>(
                    startDestination = appViewModel.currentRoute.value
                ) {
                    composable<HomeRoute.Design>(
                        enterTransition = Transitions.LeftScreenEnter,
                        exitTransition = Transitions.LeftScreenExit,
                        popEnterTransition = Transitions.RightScreenPopEnter,
                        popExitTransition = Transitions.LeftScreenPopExit
                    ) {
                        LaunchedEffect(true) {
                            appViewModel.updateRoute(HomeRoute.Design)
                        }
                        DesignScreen(viewModel = designViewModel)
                    }

                    composable<HomeRoute.Match>(
                        enterTransition = Transitions.RightScreenEnter,
                        exitTransition = Transitions.RightScreenExit,
                        popEnterTransition = Transitions.RightScreenPopEnter,
                        popExitTransition = Transitions.RightScreenPopExit
                    ) {
                        LaunchedEffect(true) {
                            appViewModel.updateRoute(HomeRoute.Match)
                        }
                        MatchScreen(viewModel = matchViewModel) { }
                    }


                    composable<HomeRoute.Profile>(
                        enterTransition = Transitions.RightScreenEnter,
                        exitTransition = Transitions.LeftScreenExit,
                        popEnterTransition = Transitions.RightScreenPopEnter,
                        popExitTransition = Transitions.LeftScreenPopExit
                    ) {
                        LaunchedEffect(true) {
                            appViewModel.updateRoute(HomeRoute.Profile)
                        }
                        ProfileScreen(viewModel = profileViewModel)
                    }
                }
            }
            BottomNavBar(navController, appViewModel.currentRoute.value)
        }
    }
}

private fun navigateTo(navController: NavController, current: HomeRoute, destination: HomeRoute) {
    if (current != destination) {
        navController.navigate(destination)
    }
}

@Composable
fun BottomNavBar(navController: NavController, currentRoute: HomeRoute) {
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
                navigateTo(navController, currentRoute, HomeRoute.Design)
            }.Draw()

            Avatar.IconAvatar(
                icon = TablerIcons.BallFootball,
                tint = colorGrey900(),
                background = Color.Transparent,
                avatarSize = AvatarSize.Big,
            ) {
                navigateTo(navController, currentRoute, HomeRoute.Match)
            }.Draw()
        }
    }
}
