package com.diego.futty.setup.profile.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.diego.futty.setup.view.SetupRoute

class ProfileViewModel : ProfileViewContract, ViewModel() {

    private var _navigate: (SetupRoute) -> Unit = {}
    private var _back: () -> Unit = {}

    fun setup(
        navController: NavHostController,
        onBack: () -> Unit,
    ) {
        _navigate = { navController.navigate(it) }
        _back = onBack
    }

    override fun onSettingsClicked() {
        _navigate(SetupRoute.Setting)
    }

    override fun onBackClicked() {
        _back()
    }
}
