package com.diego.futty.design.presentation.preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.diego.futty.core.presentation.theme.Error
import com.diego.futty.core.presentation.theme.ErrorDark
import com.diego.futty.core.presentation.theme.FuttyTheme
import com.diego.futty.core.presentation.theme.Grey0
import com.diego.futty.core.presentation.theme.Info
import com.diego.futty.core.presentation.theme.InfoDark
import com.diego.futty.core.presentation.theme.SuccessDark
import com.diego.futty.core.presentation.theme.SuccessLight
import com.diego.futty.design.presentation.component.avatar.Avatar
import com.diego.futty.design.presentation.component.avatar.AvatarSize
import compose.icons.TablerIcons
import compose.icons.tablericons.BrandLinkedin
import compose.icons.tablericons.BrandPinterest
import compose.icons.tablericons.BrandSpotify
import futty.composeapp.generated.resources.Res
import futty.composeapp.generated.resources.book_error_2
import futty.composeapp.generated.resources.girasoles
import org.jetbrains.compose.resources.painterResource

@Preview
@Composable
private fun ImageAvatarSmallPreview() {
    FuttyTheme {
        Avatar.ImageAvatar(
            image = painterResource(Res.drawable.book_error_2),
            avatarSize = AvatarSize.Small
        ).Draw()
    }
}

@Preview
@Composable
private fun ImageAvatarMediumPreview() {
    FuttyTheme {
        Avatar.ImageAvatar(
            image = painterResource(Res.drawable.book_error_2),
            avatarSize = AvatarSize.Medium
        ).Draw()
    }
}

@Preview
@Composable
private fun ImageAvatarBigPreview() {
    FuttyTheme {
        Avatar.ImageAvatar(
            image = painterResource(Res.drawable.book_error_2),
            avatarSize = AvatarSize.Big
        ).Draw()
    }
}

@Preview
@Composable
private fun FullImageAvatarSmallPreview() {
    FuttyTheme {
        Avatar.FullImageAvatar(
            image = painterResource(Res.drawable.girasoles),
            avatarSize = AvatarSize.Small
        ).Draw()
    }
}

@Preview
@Composable
private fun FullImageAvatarMediumPreview() {
    FuttyTheme {
        Avatar.FullImageAvatar(
            image = painterResource(Res.drawable.girasoles),
            avatarSize = AvatarSize.Medium
        ).Draw()
    }
}

@Preview
@Composable
private fun FullImageAvatarBigPreview() {
    FuttyTheme {
        Avatar.FullImageAvatar(
            image = painterResource(Res.drawable.girasoles),
            avatarSize = AvatarSize.Big
        ).Draw()
    }
}

@Preview
@Composable
private fun IconAvatarSmallPreview() {
    FuttyTheme {
        Avatar.IconAvatar(
            icon = TablerIcons.BrandSpotify,
            tint = Grey0,
            background = SuccessLight,
            avatarSize = AvatarSize.Small,
        ).Draw()
    }
}

@Preview
@Composable
private fun IconAvatarMediumPreview() {
    FuttyTheme {
        Avatar.IconAvatar(
            icon = TablerIcons.BrandLinkedin,
            tint = Grey0,
            background = Info,
            avatarSize = AvatarSize.Medium,
        ).Draw()
    }
}

@Preview
@Composable
private fun IconAvatarBigPreview() {
    FuttyTheme {
        Avatar.IconAvatar(
            icon = TablerIcons.BrandPinterest,
            tint = Grey0,
            background = Error,
            avatarSize = AvatarSize.Big,
        ).Draw()
    }
}



@Preview
@Composable
private fun InitialsAvatarSmallPreview() {
    FuttyTheme {
        Avatar.InitialsAvatar(
            initials = "DD",
            tint = Grey0,
            background = SuccessDark,
            avatarSize = AvatarSize.Small,
        ).Draw()
    }
}

@Preview
@Composable
private fun InitialsAvatarMediumPreview() {
    FuttyTheme {
        Avatar.InitialsAvatar(
            initials = "JC",
            tint = Grey0,
            background = InfoDark,
            avatarSize = AvatarSize.Medium,
        ).Draw()
    }
}

@Preview
@Composable
private fun InitialsAvatarBigPreview() {
    FuttyTheme {
        Avatar.InitialsAvatar(
            initials = "DM",
            tint = Grey0,
            background = ErrorDark,
            avatarSize = AvatarSize.Big,
        ).Draw()
    }
}