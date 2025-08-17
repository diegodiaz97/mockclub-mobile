package com.diego.futty.setup.profile.presentation.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.adamglin.PhosphorIcons
import com.adamglin.phosphoricons.Bold
import com.adamglin.phosphoricons.bold.Calendar
import com.diego.futty.core.presentation.theme.colorGrey500
import com.diego.futty.core.presentation.theme.colorGrey900
import com.diego.futty.core.presentation.theme.toColor
import com.diego.futty.core.presentation.utils.UserTypes.USER_TYPE_PRO
import com.diego.futty.core.presentation.utils.getFullMonthLabel
import com.diego.futty.home.design.presentation.component.avatar.Avatar
import com.diego.futty.home.design.presentation.component.avatar.AvatarSize
import com.diego.futty.home.design.presentation.component.pro.VerifiedIcon
import com.diego.futty.setup.profile.presentation.viewmodel.ProfileViewModel

@Composable
fun UserMainInfo(viewModel: ProfileViewModel) {
    val user = viewModel.user.value
    AnimatedVisibility(user != null) {
        Row(
            modifier = Modifier.padding(top = 12.dp).padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Avatar.ProfileAvatar(
                    imageUrl = viewModel.urlImage.value,
                    initials = viewModel.initials.value,
                    background = user?.profileImage?.background?.toColor(),
                    avatarSize = AvatarSize.Extra,
                    onClick = { viewModel.showUpdateImage() }
                ).Draw()
                if (viewModel.userId.value.isEmpty()) {
                    Text(
                        modifier = Modifier
                            .clickable { viewModel.onEditClicked() },
                        text = "Editar",
                        style = typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = colorGrey900()
                    )
                }
            }

            Column(
                modifier = Modifier.align(Alignment.Top),
                verticalArrangement = Arrangement.SpaceAround
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        modifier = Modifier.padding(start = 1.dp),
                        text = "${user?.name} ${user?.lastName}",
                        style = typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = colorGrey900()
                    )
                    if (user?.userType == USER_TYPE_PRO) {
                        VerifiedIcon(Modifier.padding(top = 4.dp))
                    }
                }
                if (user?.description != null) {
                    Text(
                        modifier = Modifier.fillMaxWidth().padding(start = 1.dp),
                        text = user.description,
                        style = typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = colorGrey500()
                    )
                }
                Row(
                    modifier = Modifier.padding(top = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        modifier = Modifier.size(16.dp),
                        imageVector = PhosphorIcons.Bold.Calendar,
                        tint = colorGrey500(),
                        contentDescription = null
                    )
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "Desde el ${user?.creationDate?.getFullMonthLabel()}",
                        style = typography.titleSmall,
                        fontWeight = FontWeight.Normal,
                        color = colorGrey500()
                    )
                }
            }
        }
    }
}
