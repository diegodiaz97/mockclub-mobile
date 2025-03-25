package com.diego.futty.home.feed.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.diego.futty.home.view.HomeRoute

class FeedViewModel : FeedViewContract, ViewModel() {
    private var _navigate: (HomeRoute) -> Unit = {}

    fun setup(navController: NavHostController) {
        _navigate = { navController.navigate(it) }
    }

    override fun onProfilePressed() {
        _navigate(HomeRoute.Setup)
    }
}
