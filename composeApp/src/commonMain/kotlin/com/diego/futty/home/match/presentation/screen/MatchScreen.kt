package com.diego.futty.home.match.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.diego.futty.core.presentation.maps.MapView
import com.diego.futty.core.presentation.theme.colorGrey0
import com.diego.futty.home.design.presentation.component.banner.BannerUIData
import com.diego.futty.home.design.presentation.component.pager.FullScreenPager
import com.diego.futty.home.design.presentation.component.topbar.TopBar
import com.diego.futty.home.design.presentation.component.topbar.TopBarActionType
import com.diego.futty.home.match.presentation.viewmodel.MatchViewModel
import compose.icons.TablerIcons
import compose.icons.tablericons.Menu2
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MatchScreen(viewModel: MatchViewModel = koinViewModel()) {
    Scaffold(
        containerColor = colorGrey0(),
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopBar(
                modifier = Modifier
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp),
                title = "Opina",
                topBarActionType = TopBarActionType.Icon(
                    icon = TablerIcons.Menu2,
                    onClick = { }
                )
            )
        },
        content = { paddingValues ->
            MatchV2Content(paddingValues)
        },
    )
}

@Composable
private fun MatchV2Content(paddingValues: PaddingValues) {
    Column(
        modifier = Modifier
            .padding(top = paddingValues.calculateTopPadding())
            .padding(horizontal = 16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        MapView(Modifier.height(200.dp))

        FullScreenPager(
            modifier = Modifier.weight(1f).padding(bottom = 8.dp),
            items = listOf(
                BannerUIData(
                    title = "\uD83C\uDDEC\uD83C\uDDE7 Inglaterra",
                    description = "Explora el alma vibrande de Londres.", //\n\n• Historia, cultura y modernidad en cada rincón.\n\n• Viví la ciudad como un local.",
                    labelAction = "Explorar",
                    illustration = "https://viajes.nationalgeographic.com.es/medio/2023/03/24/big-ben-y-alrededores_852e28a7_475606798_230324072203_1280x841.jpg",
                    action = { },
                ),
                BannerUIData(
                    title = "\uD83C\uDDE7\uD83C\uDDF7 Brasil",
                    description = "Sentí el ritmo de Río en cada paso.", //\n\n• Playas, samba y paisajes inolvidables.\n\n• Una experiencia que te transforma.",
                    labelAction = "Explorar",
                    illustration = "https://media.staticontent.com/media/pictures/e63f71e3-03fb-4c1b-a1e6-c8b1586a7e73",
                    action = { },
                ),
                BannerUIData(
                    title = "\uD83C\uDDEA\uD83C\uDDF8 España",
                    description = "Pasión, arte y vida en cada calle.", //\n\n• Tapas, plazas y noches que no terminan.\n\n• Descubrí el corazón de España.",
                    labelAction = "Explorar",
                    illustration = "https://images.prismic.io/bounce/f9fe3657-0901-4a18-979a-9fcebb33998e_florian-wehde-WBGjg0DsO_g-unsplash.jpg?auto=compress%2Cformat&w=1466&fit=crop&ar=3%3A2",
                    action = { },
                ),
                BannerUIData(
                    title = "\uD83C\uDDF8\uD83C\uDDEA Suecia",
                    description = "Islas, diseño y naturaleza en perfecta armonía.", //\n\n• Una ciudad moderna con alma nórdica.\n\n• Descubrí la elegancia sueca a tu ritmo.",
                    labelAction = "Explorar",
                    illustration = "https://content.r9cdn.net/rimg/dimg/1a/c4/37641f92-lm-10514-16dc9dfdfe0.jpg?crop=true&width=1020&height=498",
                    action = { },
                ),
                BannerUIData(
                    title = "\uD83C\uDDE6\uD83C\uDDFA Australia",
                    description = "Donde la ciudad abraza el mar.", //\n\n• Surf, arquitectura y aventuras únicas.\n\n• Viví Sidney a tu manera.",
                    labelAction = "Explorar",
                    illustration = "https://media.iatiseguros.com/wp-content/uploads/2019/04/04011211/que-hacer-sidney-6.jpg",
                    action = { },
                )
            )
        )
    }
}
