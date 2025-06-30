package com.diego.futty.setup.profile.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.diego.futty.core.presentation.utils.UserTypes.USER_TYPE_BASIC
import com.diego.futty.home.design.presentation.component.banner.Banner
import com.diego.futty.home.design.presentation.component.banner.BannerUIData
import com.diego.futty.setup.profile.presentation.viewmodel.ProfileViewModel

@Composable
fun UpgradeBanner(viewModel: ProfileViewModel) {
    Column(
        Modifier.padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        if (viewModel.user.value?.userType == USER_TYPE_BASIC && viewModel.userId.value.isEmpty()) {
            Banner.FullImageBanner(
                BannerUIData(
                    title = "Todavía no tienes PRO",
                    description = "Descubre las mejores funcionalidades por solo $1 USD.",
                    label = "Conoce MockClubPRO",
                    illustration = "https://cdn.pixabay.com/photo/2022/09/21/17/02/blue-background-7470781_1280.jpg",
                    action = { viewModel.onVerifyClicked() },
                ),
                page = 0,
                state = rememberPagerState { 1 },
            ).Draw()
        }
    }
}
