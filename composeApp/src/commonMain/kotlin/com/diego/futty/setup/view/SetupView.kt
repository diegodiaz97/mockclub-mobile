package com.diego.futty.setup.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.diego.futty.app.presentation.view.HomeRoute
import com.diego.futty.authentication.view.AuthenticationView
import com.diego.futty.core.presentation.theme.FuttyTheme
import com.diego.futty.core.presentation.utils.SetStatusBarColor
import com.diego.futty.core.presentation.utils.Transitions
import com.diego.futty.setup.profile.presentation.screen.ProfileScreen
import com.diego.futty.setup.profile.presentation.viewmodel.ProfileViewModel
import com.diego.futty.setup.settings.presentation.screen.SettingsScreen
import com.diego.futty.setup.settings.presentation.viewmodel.SettingsViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SetupView(
    onBack: () -> Unit,
    navigateToLogin: () -> Unit
) {
    val setupViewModel = koinViewModel<SetupViewModel>()
    val profileViewModel = koinViewModel<ProfileViewModel>()
    val settingsViewModel = koinViewModel<SettingsViewModel>()

    val navController = rememberNavController()

    FuttyTheme(settingsViewModel.palette.value) {
        SetStatusBarColor(Color.Transparent)
        LaunchedEffect(true) {
            setupViewModel.setup()
            profileViewModel.setup(navController, onBack)
            settingsViewModel.setup(navController, navigateToLogin)
        }
        Column {
            NavHost(
                modifier = Modifier.fillMaxSize(),
                navController = navController,
                startDestination = SetupRoute.SetupGraph
            ) {
                navigation<SetupRoute.SetupGraph>(
                    startDestination = setupViewModel.currentRoute.value
                ) {
                    composable<SetupRoute.Profile>(
                        enterTransition = Transitions.RightScreenEnter,
                        exitTransition = Transitions.LeftScreenExit,
                        popEnterTransition = Transitions.RightScreenPopEnter,
                        popExitTransition = Transitions.LeftScreenPopExit
                    ) {
                        LaunchedEffect(true) {
                            setupViewModel.updateRoute(SetupRoute.Profile)
                        }
                        ProfileScreen(viewModel = profileViewModel)
                    }

                    composable<SetupRoute.Setting>(
                        enterTransition = Transitions.RightScreenEnter,
                        exitTransition = Transitions.LeftScreenExit,
                        popEnterTransition = Transitions.RightScreenPopEnter,
                        popExitTransition = Transitions.LeftScreenPopExit
                    ) {
                        LaunchedEffect(true) {
                            setupViewModel.updateRoute(SetupRoute.Setting)
                        }
                        SettingsScreen(viewModel = settingsViewModel)
                    }

                    composable<SetupRoute.Authentication>(
                        enterTransition = Transitions.RightScreenEnter,
                        exitTransition = Transitions.LeftScreenExit,
                        popEnterTransition = Transitions.RightScreenPopEnter,
                        popExitTransition = Transitions.LeftScreenPopExit
                    ) {
                        LaunchedEffect(true) {
                            setupViewModel.updateRoute(SetupRoute.Authentication)
                        }
                        AuthenticationView()
                    }
                }
            }
        }
    }
}
