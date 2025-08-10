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
import com.diego.futty.authentication.profileCreation.presentation.screen.ProfileCreationScreen
import com.diego.futty.authentication.profileCreation.presentation.viewmodel.ProfileCreationViewModel
import com.diego.futty.authentication.view.AuthenticationView
import com.diego.futty.core.presentation.theme.FuttyTheme
import com.diego.futty.core.presentation.utils.SetStatusBarColor
import com.diego.futty.core.presentation.utils.Transitions
import com.diego.futty.home.postDetail.presentation.screen.PostDetailScreen
import com.diego.futty.home.postDetail.presentation.viewmodel.PostDetailViewModel
import com.diego.futty.setup.profile.presentation.screen.ProfileScreen
import com.diego.futty.setup.profile.presentation.viewmodel.ProfileViewModel
import com.diego.futty.setup.settings.presentation.screen.SettingsScreen
import com.diego.futty.setup.settings.presentation.viewmodel.SettingsViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SetupView(
    userId: String = "",
    likes: Set<String> = emptySet(),
    onBack: (likes: Set<String>) -> Unit,
    navigateToLogin: () -> Unit,
) {
    val setupViewModel = koinViewModel<SetupViewModel>()
    val profileViewModel = koinViewModel<ProfileViewModel>()
    val profileCreationViewModel = koinViewModel<ProfileCreationViewModel>()
    val settingsViewModel = koinViewModel<SettingsViewModel>()
    val postDetailViewModel = koinViewModel<PostDetailViewModel>()

    val navController = rememberNavController()

    FuttyTheme(settingsViewModel.palette.value) {
        SetStatusBarColor(Color.Transparent)
        LaunchedEffect(true) {
            setupViewModel.setup()
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
                            profileViewModel.setup(userId, likes, navController, onBack)
                            setupViewModel.updateRoute(SetupRoute.Profile)
                        }
                        ProfileScreen(viewModel = profileViewModel)
                    }

                    composable<SetupRoute.ProfileCreation>(
                        enterTransition = Transitions.RightScreenEnter,
                        exitTransition = Transitions.LeftScreenExit,
                        popEnterTransition = Transitions.RightScreenPopEnter,
                        popExitTransition = Transitions.LeftScreenPopExit
                    ) {
                        LaunchedEffect(true) {
                            profileCreationViewModel.setup(navController, "profile")
                            setupViewModel.updateRoute(SetupRoute.ProfileCreation)
                        }
                        ProfileCreationScreen(profileCreationViewModel)
                    }

                    composable<SetupRoute.PostDetail>(
                        enterTransition = Transitions.RightScreenEnter,
                        exitTransition = Transitions.LeftScreenExit,
                        popEnterTransition = Transitions.RightScreenPopEnter,
                        popExitTransition = Transitions.LeftScreenPopExit
                    ) {
                        LaunchedEffect(true) {
                            val post = profileViewModel.openedPost.value
                            if (post != null) {
                                setupViewModel.updateRoute(SetupRoute.PostDetail)
                                postDetailViewModel.setup(post)
                            }
                        }

                        PostDetailScreen(
                            viewModel = postDetailViewModel,
                            onClose = { navController.popBackStack() },
                            onLiked = {
                                profileViewModel.openedPost.value?.let { post ->
                                    profileViewModel.onLikeClicked(post, true)
                                }
                            }
                        )
                    }

                    composable<SetupRoute.Setting>(
                        enterTransition = Transitions.RightScreenEnter,
                        exitTransition = Transitions.LeftScreenExit,
                        popEnterTransition = Transitions.RightScreenPopEnter,
                        popExitTransition = Transitions.LeftScreenPopExit
                    ) {
                        LaunchedEffect(true) {
                            settingsViewModel.setup(navController, navigateToLogin)
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
