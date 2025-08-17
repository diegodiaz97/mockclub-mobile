package com.diego.futty.setup.settings.presentation.viewmodel

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.adamglin.PhosphorIcons
import com.adamglin.phosphoricons.Bold
import com.adamglin.phosphoricons.bold.Moon
import com.adamglin.phosphoricons.bold.Sun
import com.diego.futty.core.data.local.UserPreferences
import com.diego.futty.core.domain.onError
import com.diego.futty.core.domain.onSuccess
import com.diego.futty.core.presentation.theme.DayColorScheme
import com.diego.futty.core.presentation.theme.NightColorScheme
import com.diego.futty.home.design.presentation.component.banner.Banner
import com.diego.futty.home.design.presentation.component.banner.BannerStatus
import com.diego.futty.home.design.presentation.component.banner.BannerUIData
import com.diego.futty.setup.settings.domain.repository.SettingsRepository
import com.diego.futty.setup.view.SetupRoute
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val settingsRepository: SettingsRepository,
    private val preferences: UserPreferences,
) : SettingsViewContract, ViewModel() {

    private val _palette = mutableStateOf(DayColorScheme)
    override val palette: State<ColorScheme> = _palette

    private val _topBarIcon = mutableStateOf(PhosphorIcons.Bold.Moon)
    override val topBarIcon: State<ImageVector> = _topBarIcon

    private val _banner = mutableStateOf<Banner?>(null)
    override val banner: State<Banner?> = _banner

    private var _navigate: (SetupRoute) -> Unit = {}
    private var _navigateToWelcome: () -> Unit = {}
    private var _back: () -> Unit = {}

    fun setup(
        navController: NavHostController,
        navigateToLogin: () -> Unit,
    ) {
        updatePalette()
        _navigateToWelcome = {
            navController.navigate(SetupRoute.Authentication) {
                popUpTo(0) { inclusive = true }
            }
            navigateToLogin()
        }
        _navigate = { navController.navigate(it) }
        _back = { navController.popBackStack() }
    }

    override fun onBackClicked() {
        _back()
    }

    override fun onChangeThemeClicked() {
        if (_palette.value == DayColorScheme) {
            _palette.value = NightColorScheme
            _topBarIcon.value = PhosphorIcons.Bold.Moon
        } else {
            _palette.value = DayColorScheme
            _topBarIcon.value = PhosphorIcons.Bold.Sun
        }

        val currentlyDark = preferences.isDarkModeEnabled()?.not() ?: true
        preferences.saveDarkMode(currentlyDark)
    }

    override fun onLogoutClicked() {
        _banner.value = null
        viewModelScope.launch {
            settingsRepository.logout()
                .onSuccess { _navigateToWelcome() }
                .onError {
                    _banner.value = Banner.StatusBanner(
                        BannerUIData(
                            title = "No se pudo cerrar sesión",
                            description = "Intenta más tarde.",
                            status = BannerStatus.Error
                        )
                    )
                }
        }
    }

    private fun updatePalette() {
        _palette.value = if (preferences.isDarkModeEnabled() == true) {
            _topBarIcon.value = PhosphorIcons.Bold.Moon
            NightColorScheme
        } else {
            _topBarIcon.value = PhosphorIcons.Bold.Sun
            DayColorScheme
        }
    }
}
