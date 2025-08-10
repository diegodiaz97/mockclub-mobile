package com.diego.futty.home.design.presentation.component.user

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.diego.futty.core.presentation.theme.colorGrey500
import com.diego.futty.core.presentation.theme.colorGrey900
import com.diego.futty.core.presentation.theme.toColor
import com.diego.futty.core.presentation.utils.UserTypes.USER_TYPE_PRO
import com.diego.futty.home.design.presentation.component.avatar.Avatar
import com.diego.futty.home.design.presentation.component.avatar.AvatarSize
import com.diego.futty.home.design.presentation.component.pro.VerifiedIcon
import com.diego.futty.home.feed.domain.model.User

@Composable
fun User.Draw(onClick: (() -> Unit)? = null) = Row(
    modifier = Modifier.padding(vertical = 8.dp).clickable {
        if (onClick != null) {
            onClick()
        }
    },
    horizontalArrangement = Arrangement.spacedBy(12.dp),
    verticalAlignment = Alignment.CenterVertically
) {
    Avatar.ProfileAvatar(
        modifier = Modifier.align(Alignment.Top),
        imageUrl = profileImage?.image,
        initials = profileImage?.initials,
        background = profileImage?.background?.toColor(),
        avatarSize = AvatarSize.Big,
        onClick = {
            if (onClick != null) {
                onClick()
            }
        }
    ).Draw()
    Column(verticalArrangement = Arrangement.SpaceAround) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = "$name $lastName",
                style = typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = colorGrey900()
            )
            if (userType == USER_TYPE_PRO) {
                VerifiedIcon(Modifier.padding(top = 4.dp), size = 16.dp)
            }
        }
        Text(
            text = description ?: "",
            style = typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = colorGrey500()
        )
    }
}
