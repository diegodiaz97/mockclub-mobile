package com.diego.futty.setup.profile.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.diego.futty.core.presentation.theme.colorGrey100
import com.diego.futty.home.design.presentation.component.image.AsyncImage
import com.diego.futty.home.post.domain.model.PostWithUser

@Composable
fun PostsGrid(
    posts: List<PostWithUser>?,
    onPostClicked: (PostWithUser) -> Unit,
    onScrolled: () -> Unit,
) {
    val gridState = rememberLazyGridState()

    if (posts == null) {
        // Shimmer o loading
        Column {
            repeat(8) { /*PostShimmer()*/ }
        }
        return
    }

    LaunchedEffect(gridState) {
        snapshotFlow { gridState.layoutInfo }
            .collect { layoutInfo ->
                val totalItems = layoutInfo.totalItemsCount
                val visibleItems = layoutInfo.visibleItemsInfo
                val lastVisibleItemIndex = visibleItems.lastOrNull()?.index ?: 0

                // Si estamos cerca del final, pedimos más
                if (lastVisibleItemIndex >= totalItems - 3) {
                    onScrolled()
                }
            }
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        contentPadding = PaddingValues(4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 0.dp, max = 10_000.dp) // 👈 ESTO AGREGALE
    ) {
        items(posts) { post ->
            post.DrawInGrid(onPostClicked)
        }
    }
}

@Composable
private fun PostWithUser.DrawInGrid(onPostClicked: (PostWithUser) -> Unit) {
    Box(
        modifier = Modifier
            .aspectRatio(4f / 5f) // cuadrado
            .clip(RoundedCornerShape(8.dp))
            .clickable { onPostClicked(this) }
    ) {
        AsyncImage(
            modifier = Modifier
                .fillMaxSize()
                .background(colorGrey100()),
            contentDescription = "post single image",
            image = post.imageUrls.firstOrNull()
        )
    }
}