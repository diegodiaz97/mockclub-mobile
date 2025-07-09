package com.diego.futty.home.post.presentation.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.backhandler.BackHandler
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.diego.futty.core.presentation.theme.colorError
import com.diego.futty.core.presentation.theme.colorGrey0
import com.diego.futty.core.presentation.theme.colorGrey100
import com.diego.futty.core.presentation.theme.colorGrey600
import com.diego.futty.core.presentation.theme.colorGrey900
import com.diego.futty.core.presentation.theme.toColor
import com.diego.futty.core.presentation.utils.UserTypes.USER_TYPE_PRO
import com.diego.futty.core.presentation.utils.getTimeAgoLabel
import com.diego.futty.home.design.presentation.component.avatar.Avatar
import com.diego.futty.home.design.presentation.component.avatar.AvatarSize
import com.diego.futty.home.design.presentation.component.image.PagerImages
import com.diego.futty.home.design.presentation.component.pro.VerifiedIcon
import com.diego.futty.home.design.presentation.component.topbar.TopBar
import com.diego.futty.home.design.presentation.component.topbar.TopBarActionType
import com.diego.futty.home.feed.domain.model.User
import com.diego.futty.home.post.domain.model.Post
import com.diego.futty.home.post.domain.model.PostWithUser
import compose.icons.FontAwesomeIcons
import compose.icons.TablerIcons
import compose.icons.fontawesomeicons.Regular
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.regular.Comment
import compose.icons.fontawesomeicons.regular.Heart
import compose.icons.fontawesomeicons.solid.Heart
import compose.icons.tablericons.Dots

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun PostScreen(
    postWithUser: PostWithUser?,
    onClose: () -> Unit,
    onLiked: () -> Unit,
) {
    if (postWithUser == null) return
    BackHandler {
        onClose()
    }

    val focusManager = LocalFocusManager.current

    Scaffold(
        containerColor = colorGrey0(),
        modifier = Modifier.fillMaxSize()
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                focusManager.clearFocus()
            },
        topBar = {
            TopBar(
                modifier = Modifier
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp),
                title = "${postWithUser.post.team} | ${postWithUser.post.brand}",
                topBarActionType = TopBarActionType.Icon(
                    icon = TablerIcons.Dots,
                    onClick = { }
                ),
                onBack = {
                    onClose()
                }
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(top = paddingValues.calculateTopPadding())
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                PagerImages(
                    modifier = Modifier.fillMaxSize(),
                    aspectRatio = postWithUser.post.ratio,
                    images = postWithUser.post.imageUrls,
                    index = 0
                )
                PostInformation(
                    post = postWithUser.post,
                    user = postWithUser.user,
                    onLiked = onLiked
                )
                if (postWithUser.post.text.isNotEmpty()) {
                    Text(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .padding(bottom = 8.dp)
                            .fillMaxWidth(),
                        text = postWithUser.post.text,
                        style = typography.bodyLarge,
                        fontWeight = FontWeight.Normal,
                        color = colorGrey900(),
                    )
                }
                PostComments(post = postWithUser.post, user = postWithUser.user)
            }
        },
        bottomBar = {
         /*   FlexibleBottomSheet(
                isModal = true,
                onDismiss = {

                },
                content = {

                }
            )*/
        }
    )
}

@Composable
fun PostComments(post: Post, user: User?) {
    repeat(10) {
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            HorizontalDivider(thickness = 1.dp, color = colorGrey100())
            PostInformation(
                post = post,
                user = user,
                onLiked = { }
            )
            /*Text(
                text = "Comentarios",
                style = typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
                color = colorGrey900()
            )*/
        }
    }
}

@Composable
private fun PostInformation(
    post: Post,
    user: User?,
    onLiked: () -> Unit,
) {
    Row(
        modifier = Modifier.padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Avatar.ProfileAvatar(
            imageUrl = user?.profileImage?.image,
            initials = user?.profileImage?.initials,
            background = user?.profileImage?.background?.toColor(),
            onClick = { }
        ).Draw()
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.SpaceAround
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = "${user?.name} ${user?.lastName}",
                    style = typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = colorGrey900()
                )
                if (user?.userType == USER_TYPE_PRO) {
                    VerifiedIcon(Modifier.padding(top = 4.dp), size = 16.dp)
                }
            }
            Text(
                text = post.timestamp.getTimeAgoLabel(),
                style = typography.bodySmall,
                fontWeight = FontWeight.Normal,
                color = colorGrey600()
            )
            Spacer(Modifier.height(4.dp))
        }

        PostInteractions(
            post = post,
            onLiked = onLiked,
        )
    }
}

@Composable
private fun PostInteractions(
    post: Post,
    onLiked: () -> Unit,
) {
    val hasLike = post.likedByUser
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    )
    {
        Avatar.IconAvatar(
            icon = if (hasLike) FontAwesomeIcons.Solid.Heart else FontAwesomeIcons.Regular.Heart,
            tint = if (hasLike) colorError() else colorGrey900(),
            background = colorGrey0(),
            avatarSize = AvatarSize.Atomic,
            onClick = { onLiked() }
        ).Draw()
        Text(
            text = post.likesCount.toString(),
            style = typography.bodySmall,
            fontWeight = FontWeight.Normal,
            color = if (hasLike) colorError() else colorGrey900(),
        )
        Spacer(Modifier.width(8.dp))

        Avatar.IconAvatar(
            icon = FontAwesomeIcons.Regular.Comment,
            tint = colorGrey900(),
            background = colorGrey0(),
            avatarSize = AvatarSize.Atomic,
            onClick = { }
        ).Draw()
        Text(
            text = post.commentsCount.toString(),
            style = typography.bodySmall,
            fontWeight = FontWeight.Normal,
            color = colorGrey900()
        )
    }
}
