package com.diego.futty.setup.settings.presentation.viewmodel

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.diego.futty.core.data.local.UserPreferences
import com.diego.futty.core.domain.onError
import com.diego.futty.core.domain.onSuccess
import com.diego.futty.core.presentation.theme.DayColorScheme
import com.diego.futty.core.presentation.theme.NightColorScheme
import com.diego.futty.home.design.presentation.component.banner.Banner
import com.diego.futty.home.design.presentation.component.banner.BannerStatus
import com.diego.futty.setup.settings.domain.repository.SettingsRepository
import com.diego.futty.setup.view.SetupRoute
import compose.icons.TablerIcons
import compose.icons.tablericons.Moon
import compose.icons.tablericons.Sun
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val settingsRepository: SettingsRepository,
    private val preferences: UserPreferences,
) : SettingsViewContract, ViewModel() {

    private val _palette = mutableStateOf(DayColorScheme)
    override val palette: State<ColorScheme> = _palette

    private val _topBarIcon = mutableStateOf(TablerIcons.Moon)
    override val topBarIcon: State<ImageVector> = _topBarIcon

    private val _banner = mutableStateOf<Banner?>(null)
    override val banner: State<Banner?> = _banner

    private var _navigate: (SetupRoute) -> Unit = {}
    private var _navigateToLogin: () -> Unit = {}
    private var _back: () -> Unit = {}

    fun setup(
        navController: NavHostController,
        navigateToLogin: () -> Unit,
    ) {
        updatePalette()
        _navigateToLogin = {
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
            _topBarIcon.value = TablerIcons.Moon
        } else {
            _palette.value = DayColorScheme
            _topBarIcon.value = TablerIcons.Sun
        }

        val currentlyDark = preferences.isDarkModeEnabled()?.not() ?: true
        preferences.saveDarkMode(currentlyDark)
    }

    override fun onLogoutClicked() {
        _banner.value = null
        viewModelScope.launch {
            settingsRepository.logout()
                .onSuccess { _navigateToLogin() }
                .onError {
                    _banner.value = Banner.StatusBanner(
                        title = "No se pudo cerrar sesión",
                        subtitle = "Intenta más tarde.",
                        status = BannerStatus.Error
                    )
                }
        }
    }

    private fun updatePalette() {
        _palette.value = if (preferences.isDarkModeEnabled() == true) {
            _topBarIcon.value = TablerIcons.Moon
            NightColorScheme
        } else {
            _topBarIcon.value = TablerIcons.Sun
            DayColorScheme
        }
    }
}
