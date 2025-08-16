package com.diego.futty.home.view

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.backhandler.BackHandler
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
import com.diego.futty.home.challenge.presentation.screen.ChallengeScreen
import com.diego.futty.home.challenge.presentation.viewmodel.ChallengeViewModel
import com.diego.futty.home.design.presentation.component.bottombar.BottomBarItem
import com.diego.futty.home.design.presentation.screen.DesignScreen
import com.diego.futty.home.design.presentation.viewmodel.DesignViewModel
import com.diego.futty.home.feed.presentation.screen.FeedScreen
import com.diego.futty.home.feed.presentation.viewmodel.FeedViewModel
import com.diego.futty.home.match.presentation.screen.MatchScreen
import com.diego.futty.home.match.presentation.viewmodel.MatchViewModel
import com.diego.futty.home.postCreation.presentation.screen.PostCreationScreen
import com.diego.futty.home.postCreation.presentation.viewmodel.PostCreationViewModel
import com.diego.futty.home.postDetail.presentation.screen.PostDetailScreen
import com.diego.futty.home.postDetail.presentation.viewmodel.PostDetailViewModel
import com.diego.futty.setup.view.SetupView
import com.svenjacobs.reveal.RevealCanvas
import com.svenjacobs.reveal.rememberRevealCanvasState
import com.svenjacobs.reveal.rememberRevealState
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalComposeUiApi::class)
@Composable
@Preview
fun HomeView(navigateToLogin: () -> Unit) {
    val appViewModel = koinViewModel<HomeViewModel>()
    val designViewModel = koinViewModel<DesignViewModel>()
    val feedViewModel = koinViewModel<FeedViewModel>()
    val challengeViewModel = koinViewModel<ChallengeViewModel>()
    val matchViewModel = koinViewModel<MatchViewModel>()
    val postCreationViewModel = koinViewModel<PostCreationViewModel>()
    val postDetailViewModel = koinViewModel<PostDetailViewModel>()
    val navController = rememberNavController()

    BackHandler(true) {}

    FuttyTheme(appViewModel.palette.value) {
        val revealCanvasState = rememberRevealCanvasState()
        val revealState = rememberRevealState()
        val scope = rememberCoroutineScope()

        RevealCanvas(
            modifier = Modifier,
            revealCanvasState = revealCanvasState,
        ) {
            SetStatusBarColor(colorGrey0())
            LaunchedEffect(true) {
                appViewModel.setup()
                feedViewModel.setup(navController)
                designViewModel.setup(navController)
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
                            FeedScreen(
                                revealCanvasState = revealCanvasState,
                                scope = scope,
                                revealState = revealState,
                                viewModel = feedViewModel,
                            )
                        }

                        composable<HomeRoute.Challenge>(
                            enterTransition = { EnterTransition.None }, // Sin animación de entrada
                            exitTransition = { ExitTransition.None },    // Sin animación de salida
                        ) {
                            LaunchedEffect(true) {
                                challengeViewModel.setup(navController)
                                appViewModel.updateRoute(HomeRoute.Challenge)
                            }
                            ChallengeScreen(viewModel = challengeViewModel)
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
                            MatchScreen(viewModel = matchViewModel)
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
                                userId = designViewModel.clickedUser.value,
                                onBack = { userMadeLike ->
                                    navController.popBackStack()
                                    designViewModel.resetUserId()
                                    feedViewModel.updateLikes(userMadeLike)
                                },
                                navigateToLogin = navigateToLogin,
                            )
                        }

                        composable<HomeRoute.CreatePost>(
                            enterTransition = Transitions.RightScreenEnter,
                            exitTransition = Transitions.LeftScreenExit,
                            popEnterTransition = Transitions.RightScreenPopEnter,
                            popExitTransition = Transitions.LeftScreenPopExit
                        ) {
                            LaunchedEffect(true) {
                                appViewModel.updateRoute(HomeRoute.CreatePost)
                            }

                            PostCreationScreen(
                                user = feedViewModel.user.value,
                                viewModel = postCreationViewModel,
                                onClose = { navController.popBackStack() },
                                onStartPostCreation = {
                                    feedViewModel.onStartPostCreation()
                                    navController.popBackStack()
                                },
                                onPostCreated = {
                                    feedViewModel.onPostCreated()
                                },
                            )
                        }

                        composable<HomeRoute.PostDetail>(
                            enterTransition = Transitions.RightScreenEnter,
                            exitTransition = Transitions.LeftScreenExit,
                            popEnterTransition = Transitions.RightScreenPopEnter,
                            popExitTransition = Transitions.LeftScreenPopExit
                        ) {
                            LaunchedEffect(true) {
                                val post = feedViewModel.openedPost.value
                                if (post != null) {
                                    appViewModel.updateRoute(HomeRoute.PostDetail)
                                    postDetailViewModel.setup(post)
                                }
                            }

                            PostDetailScreen(
                                viewModel = postDetailViewModel,
                                onClose = { navController.popBackStack() },
                                onLiked = {
                                    feedViewModel.openedPost.value?.let { post ->
                                        feedViewModel.onLikeClicked(post, true)
                                    }
                                },
                            )
                        }
                    }
                }

                BottomNavBar(appViewModel, navController)
                designViewModel.modal.value?.Draw()
            }
        }
    }
}

@Composable
private fun BottomNavBar(appViewModel: HomeViewModel, navController: NavController) {
    if (appViewModel.showBottomBar.value) {
        val currentRoute = appViewModel.currentRoute.value

        Column {
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth().height(1.dp),
                color = colorGrey100(),
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(colorGrey0())
                    .navigationBarsPadding()
                    .offset(y = 8.dp),
                horizontalArrangement = Arrangement.SpaceAround,
            ) {
                BottomNavScreen.allScreens.forEach { screen ->
                    BottomBarItem(
                        icon = if (currentRoute == screen.route) screen.selectedIcon else screen.icon,
                        tint = if (currentRoute == screen.route) colorGrey900() else colorGrey500(),
                        color = Color.Transparent,
                    ) {
                        if (appViewModel.canUseBottomBar.value) {
                            navigateTo(navController, currentRoute, screen.route)
                        }
                    }
                }
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
