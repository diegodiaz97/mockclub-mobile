package com.diego.futty.home.design.presentation.component.post

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.diego.futty.core.presentation.theme.Shimmer
import com.diego.futty.core.presentation.theme.colorError
import com.diego.futty.core.presentation.theme.colorGrey0
import com.diego.futty.core.presentation.theme.colorGrey100
import com.diego.futty.core.presentation.theme.colorGrey200
import com.diego.futty.core.presentation.theme.colorGrey500
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
import com.diego.futty.home.post.domain.model.Post
import com.diego.futty.home.post.domain.model.PostWithUser
import compose.icons.FontAwesomeIcons
import compose.icons.TablerIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.Comment
import compose.icons.fontawesomeicons.solid.Heart
import compose.icons.tablericons.Heart
import compose.icons.tablericons.HeartBroken
import compose.icons.tablericons.Message

@Composable
fun PostWithUser.Draw(
    hasLike: Boolean,
    hasDislike: Boolean,
    onLiked: () -> Unit,
    onDisliked: () -> Unit,
    onImageClick: (image: String) -> Unit,
    onClick: () -> Unit,
) =
    Column(modifier = Modifier.padding(vertical = 12.dp).clickable { onClick() }) {
        PostInformation(
            post = post,
            user = user,
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
        if (post.imageUrls.isNotEmpty()) {
            PostImage(post.imageUrls, onImageClick)
        }
        PostFooter(post, hasLike, hasDislike, onLiked, onDisliked)
    }

@Composable
private fun PostInformation(
    post: Post,
    user: User?,
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
    }
}

@Composable
private fun PostImage(
    images: List<String>,
    onImageClick: (image: String) -> Unit,
) {
    if (images.size > 1) {
        LazyRow(
            modifier = Modifier.height(260.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            item { Spacer(Modifier.width(8.dp)) }
            images.forEachIndexed { index, image ->
                item {
                    AsyncImage(
                        modifier = Modifier
                            .size(260.dp)
                            .clickable { onImageClick(image) }
                            .clip(RoundedCornerShape(12.dp))
                            .background(colorGrey100()),
                        contentDescription = "post list image",
                        image = image
                    )
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
                .clickable { onImageClick(image) }
                .clip(RoundedCornerShape(12.dp))
                .background(colorGrey100()),
            contentDescription = "post single image",
            image = image
        )
    }
}

@Composable
private fun PostFooter(
    post: Post,
    hasLike: Boolean,
    hasDislike: Boolean,
    onLiked: () -> Unit,
    onDisliked: () -> Unit,
) {
    Row(
        modifier = Modifier.padding(top = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp).weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        )
        {
            Avatar.IconAvatar(
                icon = FontAwesomeIcons.Solid.Heart,
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
                icon = FontAwesomeIcons.Solid.Comment,
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
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(colorGrey500())
                .padding(horizontal = 8.dp, vertical = 2.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End,
        )
        {
            Text(
                text = post.team,
                style = typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = colorGrey0()
            )
            Text(
                text = " x ",
                style = typography.labelSmall,
                fontWeight = FontWeight.Normal,
                color = colorGrey100()
            )
            Text(
                text = post.brand,
                style = typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = colorGrey0()
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
