package com.diego.futty.home.design.presentation.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.diego.futty.home.view.HomeRoute
import com.diego.futty.core.presentation.theme.AlertLight
import com.diego.futty.core.presentation.theme.ErrorLight
import com.diego.futty.core.presentation.theme.InfoLight
import com.diego.futty.core.presentation.theme.SuccessLight
import com.diego.futty.home.design.presentation.component.Chip.ChipModel
import com.diego.futty.home.design.presentation.component.banner.BannerUIData
import compose.icons.TablerIcons
import compose.icons.tablericons.BallFootball
import compose.icons.tablericons.BrandPinterest
import compose.icons.tablericons.Headphones
import compose.icons.tablericons.Message2
import compose.icons.tablericons.Palette
import compose.icons.tablericons.Pizza
import compose.icons.tablericons.Plane
import compose.icons.tablericons.School
import compose.icons.tablericons.Video
import compose.icons.tablericons.Wallet

class DesignViewModel : DesignViewContract, ViewModel() {
    private val _buttonEnabled = mutableStateOf(true)
    override val buttonEnabled: State<Boolean> = _buttonEnabled

    private val _buttonText = mutableStateOf("Continuar")
    override val buttonText: State<String> = _buttonText

    private val _bottomsheetDismissed = mutableStateOf(true)
    override val bottomsheetDismissed: State<Boolean> = _bottomsheetDismissed

    private val _chipItems = mutableStateOf(setupChipItems())
    override val chipItems: State<List<ChipModel>> = _chipItems

    private val _selectedChip = mutableStateOf(0)
    override val selectedChip: State<Int> = _selectedChip

    private var _navigate: (HomeRoute) -> Unit = {}

    fun setup(navController: NavHostController) {
        _navigate = { navController.navigate(it) }
    }

    override fun onButtonPressed() {
        _bottomsheetDismissed.value = false
    }

    override fun onProfilePressed() {
        _navigate(HomeRoute.Setup)
    }

    override fun onBottomSheetDismissed() {
        _bottomsheetDismissed.value = true
    }

    override fun getScrollableBanners() = BannerUIData(
        title = "Scrollable Banner",
        description = "Éste banner puede varias cosas en un mismo lugar. Máximo 2 líneas",
        labelAction = "Ver más",
        action = { onScrollBannerPressed("accion 1") },
    )

    override fun onScrollBannerPressed(text: String) {
        _buttonText.value = text
    }

    override fun onChipSelected(index: Int) {
        _selectedChip.value = index
    }

    private fun setupChipItems() = listOf(
        ChipModel(
            icon = TablerIcons.School,
            color = AlertLight,
            text = "Cursos"
        ),
        ChipModel(
            icon = TablerIcons.Pizza,
            color = InfoLight,
            text = "Comidas"
        ),
        ChipModel(
            icon = TablerIcons.Video,
            color = SuccessLight,
            text = "Cine"
        ),
        ChipModel(
            icon = TablerIcons.Palette,
            color = ErrorLight,
            text = "Arte"
        ),
        ChipModel(
            icon = TablerIcons.Message2,
            color = AlertLight,
            text = "Idiomas"
        ),
        ChipModel(
            icon = TablerIcons.Headphones,
            color = InfoLight,
            text = "Música"
        ),
        ChipModel(
            icon = TablerIcons.BrandPinterest,
            color = SuccessLight,
            text = "Decoración"
        ),
        ChipModel(
            icon = TablerIcons.Wallet,
            color = ErrorLight,
            text = "Finanzas"
        ),
        ChipModel(
            icon = TablerIcons.BallFootball,
            color = AlertLight,
            text = "Deportes"
        ),
        ChipModel(
            icon = TablerIcons.Plane,
            color = InfoLight,
            text = "Vacaciones"
        ),
    )
}
