package com.diego.futty.home.design.presentation.component.post

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.diego.futty.core.presentation.theme.colorGrey100
import com.diego.futty.core.presentation.theme.colorGrey600
import com.diego.futty.core.presentation.theme.colorGrey900
import com.diego.futty.core.presentation.theme.toColor
import com.diego.futty.home.design.presentation.component.avatar.Avatar
import com.diego.futty.home.feed.domain.model.ActionableImage
import com.diego.futty.home.feed.domain.model.Post
import com.diego.futty.home.feed.domain.model.ProfileImage
import org.jetbrains.compose.resources.painterResource

@Composable
fun Post.Draw() =
    Column(
        modifier = Modifier.padding(vertical = 12.dp).clickable { onClick(this) },
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        PostInformation(user.profileImage, user.name, date)
        if (text != null) {
            Text(
                modifier = Modifier.padding(horizontal = 16.dp).fillMaxWidth(),
                text = text,
                style = typography.bodyLarge,
                fontWeight = FontWeight.Normal,
                color = colorGrey900(),
            )
        }
        if (images != null) {
            PostImage(images)
        }
    }

@Composable
private fun PostInformation(
    profileImage: ProfileImage,
    title: String,
    subtitle: String,
) {
    Row(
        modifier = Modifier.padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val image = profileImage.image
        Avatar.ProfileAvatar(
            image = if (image != null) painterResource(image) else null,
            initials = profileImage.initials,
            background = profileImage.background?.toColor(),
            onClick = { }
        ).Draw()
        Column(verticalArrangement = Arrangement.SpaceAround) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = title,
                style = typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
                color = colorGrey900()
            )
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = subtitle,
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
    images: List<ActionableImage>,
) {
    if (images.size > 1) {
        LazyRow(
            modifier = Modifier.height(260.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            item { Spacer(Modifier.width(8.dp)) }
            images.forEach { image ->
                item {
                    Image(
                        painter = painterResource(image.image),
                        contentDescription = "",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(260.dp)
                            .clickable { image.onClick(image) }
                            .clip(RoundedCornerShape(12.dp))
                            .background(colorGrey100())
                    )
                }
            }
            item { Spacer(Modifier.width(8.dp)) }
        }
    } else {
        val image = images.last()
        Image(
            painter = painterResource(image.image),
            contentDescription = "",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .clickable { image.onClick(image) }
                .clip(RoundedCornerShape(12.dp))
                .background(colorGrey100())
        )
    }
}
