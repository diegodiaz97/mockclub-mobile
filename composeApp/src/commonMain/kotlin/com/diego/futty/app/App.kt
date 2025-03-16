package com.diego.futty.app

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.diego.futty.core.presentation.theme.FuttyTheme
import com.diego.futty.core.presentation.theme.colorGrey0
import com.diego.futty.design.presentation.screen.DesignScreen
import com.diego.futty.design.presentation.viewmodel.DesignViewModel
import com.diego.futty.design.utils.SetStatusBarColor
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
@Preview
fun App() {
    val designViewModel = koinViewModel<DesignViewModel>()

    FuttyTheme(designViewModel.palette.value) {
        SetStatusBarColor(colorGrey0())
        val navController = rememberNavController()

        NavHost(
            navController = navController,
            startDestination = Route.Graph
        ) {
            navigation<Route.Graph>(
                startDestination = Route.Design
            ) {
                composable<Route.Design>(
                    exitTransition = { slideOutHorizontally() },
                    popEnterTransition = { slideInHorizontally() }
                ) {
                    LaunchedEffect(true) {
                        designViewModel.setup()
                    }
                    DesignScreen(
                        viewModel = designViewModel,
                        onBack = { navController.navigate(Route.Match) }
                    )
                }
            }
        }

    }
}

@Composable
private inline fun <reified T : ViewModel> NavBackStackEntry.sharedKoinViewModel(
    navController: NavController
): T {
    val navGraphRoute = destination.parent?.route ?: return koinViewModel<T>()
    val parentEntry = remember(this) {
        navController.getBackStackEntry(navGraphRoute)
    }
    return koinViewModel(
        viewModelStoreOwner = parentEntry
    )
}
