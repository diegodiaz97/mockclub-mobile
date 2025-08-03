package com.diego.futty.home.challenge.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.diego.futty.home.view.HomeRoute

class ChallengeViewModel(
    // private val challengeRepository: ChallengeRepository
): ChallengeViewContract, ViewModel() {

    private var _navigate: (HomeRoute) -> Unit = {}

    fun setup(navController: NavHostController) {
        _navigate = { navController.navigate(it) }
    }
}
