package com.diego.futty.setup.profile.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.diego.futty.core.presentation.theme.colorGrey100
import com.diego.futty.core.presentation.theme.colorGrey900
import com.diego.futty.home.design.presentation.component.post.Draw
import com.diego.futty.home.design.presentation.component.post.PostShimmer
import com.diego.futty.home.post.domain.model.PostWithUser

@Composable
fun PostsList(
    posts: List<PostWithUser>?,
    topListComponents: @Composable () -> Unit = { },
    bottomListComponents: @Composable () -> Unit = { },
    onPostClicked: (PostWithUser) -> Unit,
    onImageClicked: (() -> Unit)? = null,
    onScrolled: () -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item { topListComponents() }

        if (posts != null) {
            posts.forEachIndexed { index, post ->
                item {
                    post.Draw(
                        hasLike = true,
                        hasDislike = false,
                        onLiked = {},
                        onDisliked = {},
                        onImageClick = {
                            if (onImageClicked != null) {
                                onImageClicked()
                            } else {
                                onPostClicked(post)
                            }
                        },
                        onClick = { onPostClicked(post) },
                    )
                    if (index < posts.lastIndex) {
                        HorizontalDivider(thickness = 1.dp, color = colorGrey100())
                    }
                    if (index > 0 && index % 3 == 0) {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(colorGrey100())
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                            color = colorGrey900(),
                            style = typography.labelLarge,
                            text = "Publicidad n°${index / 3}"
                        )
                        if (index < posts.lastIndex) {
                            HorizontalDivider(thickness = 1.dp, color = colorGrey100())
                        }
                    }

                    // Detectar si estamos cerca del final
                    if (index >= posts.size - 1) {
                        onScrolled()
                    }
                }
            }
        } else {
            item { Column { repeat(8) { PostShimmer() } } }
        }
        item { bottomListComponents() }
    }
}
