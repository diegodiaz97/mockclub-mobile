package com.diego.futty.home.design.presentation.component.post

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.adamglin.PhosphorIcons
import com.adamglin.phosphoricons.Bold
import com.adamglin.phosphoricons.Fill
import com.adamglin.phosphoricons.bold.ChatCircle
import com.adamglin.phosphoricons.bold.DotsThree
import com.adamglin.phosphoricons.bold.Heart
import com.adamglin.phosphoricons.bold.PaperPlaneTilt
import com.adamglin.phosphoricons.fill.Heart
import com.diego.futty.core.presentation.theme.Grey0
import com.diego.futty.core.presentation.theme.Grey800
import com.diego.futty.core.presentation.theme.Shimmer
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
import com.diego.futty.home.design.presentation.component.image.AsyncImage
import com.diego.futty.home.design.presentation.component.pro.VerifiedIcon
import com.diego.futty.home.feed.domain.model.User
import com.diego.futty.home.postCreation.domain.model.Post
import com.diego.futty.home.postCreation.domain.model.PostWithExtras

@Composable
fun PostItem(
    postWithExtras: PostWithExtras,
    onLiked: () -> Unit,
    onImageClick: (image: String) -> Unit,
    onUserClicked: () -> Unit,
    onOptionsClicked: () -> Unit,
    onClick: () -> Unit,
) {
    val post = postWithExtras.post
    val user = postWithExtras.user
    Column(modifier = Modifier.padding(vertical = 12.dp).clickable { onClick() }) {
        PostInformation(
            post = post,
            user = user,
            onUserClicked = onUserClicked,
            onOptionsClicked = onOptionsClicked,
        )
        if (post.text.isNotEmpty()) {
            Text(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 8.dp)
                    .fillMaxWidth(),
                text = post.text,
                style = typography.bodyLarge,
                fontWeight = FontWeight.Normal,
                color = colorGrey900(),
            )
        }
        if (postWithExtras.images.isNotEmpty()) {
            PostImage(
                post = postWithExtras,
                onImageClick = onImageClick,
                onImageDoubleClick = onLiked,
            )
        }
        PostFooter(postWithExtras, onLiked)
    }
}

@Composable
private fun PostInformation(
    post: Post,
    user: User,
    onUserClicked: () -> Unit,
    onOptionsClicked: () -> Unit,
) {
    Row(
        modifier = Modifier.padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Avatar.ProfileAvatar(
            imageUrl = user.profileImage?.image,
            initials = user.profileImage?.initials,
            background = user.profileImage?.background?.toColor(),
            onClick = { onUserClicked() }
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
                    text = "${user.name} ${user.lastName}",
                    style = typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = colorGrey900()
                )
                if (user.userType == USER_TYPE_PRO) {
                    VerifiedIcon(Modifier.padding(top = 4.dp), size = 16.dp)
                }
                Spacer(Modifier.weight(1f))
                Avatar.IconAvatar(
                    icon = PhosphorIcons.Bold.DotsThree,
                    tint = colorGrey900(),
                    background = colorGrey0(),
                    avatarSize = AvatarSize.Small,
                    onClick = { onOptionsClicked() }
                ).Draw()
            }
            Text(
                text = post.createdAt.getTimeAgoLabel(),
                style = typography.bodySmall,
                fontWeight = FontWeight.Normal,
                color = colorGrey600()
            )
            Spacer(Modifier.height(4.dp))
        }
    }
}

@Composable
private fun PostImage(
    post: PostWithExtras,
    onImageClick: (image: String) -> Unit,
    onImageDoubleClick: () -> Unit,
) {
    val images = post.images
    if (images.size > 1) {
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            item { Spacer(Modifier.width(8.dp)) }
            images.forEachIndexed { index, image ->
                item {
                    Box(
                        modifier = Modifier
                            .width(260.dp)
                            .pointerInput(Unit) {
                                detectTapGestures(
                                    onDoubleTap = { onImageDoubleClick() },
                                    onLongPress = { onImageDoubleClick() },
                                    onTap = { onImageClick(image) }
                                )
                            }
                            .clip(RoundedCornerShape(12.dp))
                            .background(colorGrey100()),
                        contentAlignment = Alignment.TopEnd
                    ) {
                        AsyncImage(
                            modifier = Modifier
                                .fillMaxSize()
                                .aspectRatio(post.post.ratio),
                            contentDescription = "post list image",
                            image = image
                        )
                        Text(
                            modifier = Modifier
                                .padding(8.dp)
                                .clip(CircleShape)
                                .background(Grey800.copy(alpha = 0.4f))
                                .padding(horizontal = 8.dp, vertical = 4.dp),
                            text = "${index+1}/${images.size}",
                            style = typography.labelSmall,
                            fontWeight = FontWeight.Normal,
                            color = Grey0,
                        )
                    }
                }
            }
            item { Spacer(Modifier.width(8.dp)) }
        }
    } else {
        val image = images.last()
        AsyncImage(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onDoubleTap = { onImageDoubleClick() },
                        onLongPress = { onImageDoubleClick() },
                        onTap = { onImageClick(image) }
                    )
                }
                .clip(RoundedCornerShape(12.dp))
                .background(colorGrey100()),
            contentDescription = "post single image",
            image = image
        )
    }
}

@Composable
private fun PostFooter(
    post: PostWithExtras,
    onLiked: () -> Unit,
) {
    val hasLike = post.likedByCurrentUser
    Row(
        modifier = Modifier.padding(top = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp).weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(2.dp)
        )
        {
            Avatar.IconAvatar(
                icon = if (hasLike) PhosphorIcons.Fill.Heart else PhosphorIcons.Bold.Heart,
                tint = if (hasLike) colorError() else colorGrey900(),
                background = colorGrey0(),
                avatarSize = AvatarSize.Small,
                onClick = { onLiked() }
            ).Draw()
            Text(
                text = post.likeCount.toString(),
                style = typography.bodyLarge,
                fontWeight = FontWeight.Normal,
                color = if (hasLike) colorError() else colorGrey900(),
            )

            Spacer(Modifier.width(8.dp))

            Avatar.IconAvatar(
                icon = PhosphorIcons.Bold.ChatCircle,
                tint = colorGrey900(),
                background = colorGrey0(),
                avatarSize = AvatarSize.Small,
                onClick = { }
            ).Draw()
            Text(
                text = post.commentCount.toString(),
                style = typography.bodyLarge,
                fontWeight = FontWeight.Normal,
                color = colorGrey900()
            )

            Spacer(Modifier.width(8.dp))

            Avatar.IconAvatar(
                icon = PhosphorIcons.Bold.PaperPlaneTilt,
                tint = colorGrey900(),
                background = colorGrey0(),
                avatarSize = AvatarSize.Small,
                onClick = { /*onShare()*/ }
            ).Draw()

            Spacer(Modifier.weight(1f))

            PostLogos(
                modifier = Modifier,
                teamLogo = post.post.teamLogo,
                brandLogo = post.post.brandLogo,
                designerLogo = post.post.designerLogo,
            )
        }
    }
}

@Composable
fun PostShimmer() {
    Column(
        modifier = Modifier.padding(vertical = 12.dp, horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Avatar.ProfileAvatar(
                imageUrl = null,
                initials = null,
            ).Draw()
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Shimmer(modifier = Modifier.height(12.dp).width(70.dp).clip(CircleShape))
                Shimmer(modifier = Modifier.height(12.dp).width(100.dp).clip(CircleShape))
            }
        }
        Shimmer(modifier = Modifier.height(14.dp).width(300.dp).clip(CircleShape))
        Shimmer(modifier = Modifier.height(260.dp).fillMaxWidth().clip(RoundedCornerShape(12.dp)))
        HorizontalDivider(thickness = 1.dp, color = colorGrey100())
    }
}
