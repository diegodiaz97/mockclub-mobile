package com.diego.futty.design.presentation.preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.diego.futty.core.presentation.theme.AlertLight
import com.diego.futty.core.presentation.theme.DayColorScheme
import com.diego.futty.core.presentation.theme.ErrorLight
import com.diego.futty.core.presentation.theme.FuttyTheme
import com.diego.futty.core.presentation.theme.InfoLight
import com.diego.futty.core.presentation.theme.NightColorScheme
import com.diego.futty.core.presentation.theme.SuccessLight
import com.diego.futty.home.design.presentation.component.chip.ChipModel
import com.diego.futty.home.design.presentation.component.flowrow.FlowList
import compose.icons.TablerIcons
import compose.icons.tablericons.BrandSpotify

@Preview
@Composable
private fun NightChipsPreview() {
    FuttyTheme(NightColorScheme) {
        FlowList(chipItems, 0) { index ->

        }
    }
}

@Preview
@Composable
private fun DayChipsPreview() {
    FuttyTheme(DayColorScheme) {
        FlowList(chipItems, 3) { index ->

        }
    }
}

private val chipItems = listOf(
    ChipModel(
        icon = TablerIcons.BrandSpotify,
        color = AlertLight,
        text = "Deportes"
    ),
    ChipModel(
        icon = TablerIcons.BrandSpotify,
        color = InfoLight,
        text = "Comidas"
    ),
    ChipModel(
        icon = TablerIcons.BrandSpotify,
        color = SuccessLight,
        text = "Cine"
    ),
    ChipModel(
        icon = TablerIcons.BrandSpotify,
        color = ErrorLight,
        text = "Arte"
    ),
    ChipModel(
        icon = TablerIcons.BrandSpotify,
        color = AlertLight,
        text = "Idiomas"
    ),
    ChipModel(
        icon = TablerIcons.BrandSpotify,
        color = InfoLight,
        text = "Música"
    ),
    ChipModel(
        icon = TablerIcons.BrandSpotify,
        color = SuccessLight,
        text = "Decoración"
    ),
    ChipModel(
        icon = TablerIcons.BrandSpotify,
        color = ErrorLight,
        text = "Finanzas"
    ),
    ChipModel(
        icon = TablerIcons.BrandSpotify,
        color = AlertLight,
        text = "Cursos"
    ),
)