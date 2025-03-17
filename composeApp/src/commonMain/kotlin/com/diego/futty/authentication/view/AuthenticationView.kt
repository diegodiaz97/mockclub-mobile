package com.diego.futty.authentication.view

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.diego.futty.app.App
import com.diego.futty.authentication.login.presentation.screen.LoginScreen
import com.diego.futty.authentication.login.presentation.viewmodel.LoginViewModel
import com.diego.futty.authentication.signup.presentation.screen.SignupScreen
import com.diego.futty.authentication.signup.presentation.viewmodel.SignupViewModel
import com.diego.futty.core.presentation.theme.FuttyTheme
import com.diego.futty.design.utils.SetStatusBarColor
import com.diego.futty.design.utils.Transitions
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AuthenticationView() {
    val authenticationViewModel = koinViewModel<AuthenticationViewModel>()
    //val welcomeViewModel = koinViewModel<AppViewModel>()
    val signupViewModel = koinViewModel<SignupViewModel>()
    val loginViewModel = koinViewModel<LoginViewModel>()
    //val recoveryViewModel = koinViewModel<MatchViewModel>()
    val navController = rememberNavController()

    FuttyTheme(authenticationViewModel.palette.value) {
        SetStatusBarColor(Color.Transparent)
        LaunchedEffect(true) {
            authenticationViewModel.setup()
            //designViewModel.setup()
            signupViewModel.setup()
            loginViewModel.setup(navController)
            //recoveryViewModel.setup()
        }

        Column {
            NavHost(
                modifier = Modifier.weight(1f),
                navController = navController,
                startDestination = AuthenticationRoute.Graph
            ) {
                navigation<AuthenticationRoute.Graph>(
                    startDestination = authenticationViewModel.currentRoute.value
                ) {
                    composable<AuthenticationRoute.Welcome>(
                        enterTransition = Transitions.LeftScreenEnter,
                        exitTransition = Transitions.LeftScreenExit,
                        popEnterTransition = Transitions.LeftScreenPopEnter,
                        popExitTransition = Transitions.LeftScreenPopExit
                    ) {
                        LaunchedEffect(true) {
                            authenticationViewModel.updateRoute(AuthenticationRoute.Welcome)
                        }
                        // WelcomeScreen(viewModel = welcomeViewModel)
                    }

                    composable<AuthenticationRoute.Signup>(
                        enterTransition = Transitions.RightScreenEnter,
                        exitTransition = Transitions.RightScreenExit,
                        popEnterTransition = Transitions.RightScreenPopEnter,
                        popExitTransition = Transitions.RightScreenPopExit
                    ) {
                        LaunchedEffect(true) {
                            authenticationViewModel.updateRoute(AuthenticationRoute.Signup)
                        }
                        SignupScreen(viewModel = signupViewModel)
                    }

                    composable<AuthenticationRoute.Login>(
                        enterTransition = Transitions.LeftScreenEnter,
                        exitTransition = Transitions.LeftScreenExit,
                        popEnterTransition = Transitions.LeftScreenPopEnter,
                        popExitTransition = Transitions.LeftScreenPopExit
                    ) {
                        LaunchedEffect(true) {
                            authenticationViewModel.updateRoute(AuthenticationRoute.Login)
                        }
                        LoginScreen(viewModel = loginViewModel)
                    }

                    composable<AuthenticationRoute.Recovery>(
                        enterTransition = Transitions.LeftScreenEnter,
                        exitTransition = Transitions.LeftScreenExit,
                        popEnterTransition = Transitions.LeftScreenPopEnter,
                        popExitTransition = Transitions.LeftScreenPopExit
                    ) {
                        LaunchedEffect(true) {
                            authenticationViewModel.updateRoute(AuthenticationRoute.Recovery)
                        }
                        // WelcomeScreen(viewModel = welcomeViewModel)
                    }

                    composable<AuthenticationRoute.Home>(
                        enterTransition = Transitions.LeftScreenEnter,
                        exitTransition = Transitions.LeftScreenExit,
                        popEnterTransition = Transitions.LeftScreenPopEnter,
                        popExitTransition = Transitions.LeftScreenPopExit
                    ) {
                        LaunchedEffect(true) {
                            authenticationViewModel.updateRoute(AuthenticationRoute.Home)
                        }
                        App()
                    }
                }
            }
        }
    }
}