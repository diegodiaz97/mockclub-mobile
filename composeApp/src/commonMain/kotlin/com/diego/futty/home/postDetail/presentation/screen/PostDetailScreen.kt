package com.diego.futty.home.postDetail.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.backhandler.BackHandler
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.diego.futty.core.presentation.theme.colorError
import com.diego.futty.core.presentation.theme.colorGrey0
import com.diego.futty.core.presentation.theme.colorGrey100
import com.diego.futty.core.presentation.theme.colorGrey400
import com.diego.futty.core.presentation.theme.colorGrey600
import com.diego.futty.core.presentation.theme.colorGrey900
import com.diego.futty.core.presentation.theme.toColor
import com.diego.futty.core.presentation.utils.UserTypes.USER_TYPE_PRO
import com.diego.futty.core.presentation.utils.getTimeAgoLabel
import com.diego.futty.home.design.presentation.component.avatar.Avatar
import com.diego.futty.home.design.presentation.component.avatar.AvatarSize
import com.diego.futty.home.design.presentation.component.image.PagerImages
import com.diego.futty.home.design.presentation.component.input.TextInput
import com.diego.futty.home.design.presentation.component.pro.VerifiedIcon
import com.diego.futty.home.design.presentation.component.topbar.TopBar
import com.diego.futty.home.design.presentation.component.topbar.TopBarActionType
import com.diego.futty.home.feed.domain.model.User
import com.diego.futty.home.feed.presentation.screen.LoadingProgress
import com.diego.futty.home.postCreation.domain.model.CommentWithUser
import com.diego.futty.home.postCreation.domain.model.Post
import com.diego.futty.home.postDetail.presentation.viewmodel.PostDetailViewModel
import compose.icons.FontAwesomeIcons
import compose.icons.TablerIcons
import compose.icons.fontawesomeicons.Regular
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.regular.Comment
import compose.icons.fontawesomeicons.regular.Heart
import compose.icons.fontawesomeicons.solid.Heart
import compose.icons.tablericons.ArrowUp
import compose.icons.tablericons.Dots

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun PostDetailScreen(
    viewModel: PostDetailViewModel,
    onClose: () -> Unit,
    onLiked: () -> Unit,
) {
    val postWithUser = viewModel.post.value ?: return

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
                    .padding(
                        top = paddingValues.calculateTopPadding(),
                        bottom = paddingValues.calculateBottomPadding()
                    )
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
                    onLiked = {
                        viewModel.onLikeClicked()
                        onLiked()
                    }
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
                LoadingProgress(
                    text = "Agregando comentario",
                    progress = viewModel.commentCreationProgress.value
                )
                PostComments(viewModel)
            }
        },
        bottomBar = {
            CommentInput(focusManager) { comment -> viewModel.onCommentClicked(comment) }
        }
    )
}

@Composable
fun CommentInput(
    focusManager: FocusManager,
    onComment: (String) -> Unit,
) {
    var comment by remember { mutableStateOf("") }

    Row(
        modifier = Modifier.fillMaxWidth()
            .clip(RoundedCornerShape(36.dp))
            .background(colorGrey100())
            .padding(horizontal = 16.dp, vertical = 24.dp)
            .imePadding()
            .navigationBarsPadding(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        TextInput.Input(
            modifier = Modifier.weight(1f),
            input = comment,
            placeholder = "Agregar Comentario",
            background = colorGrey0(),
            onFocusChanged = { },
            onTextChangeAction = { comment = it }
        ).Draw()
        Avatar.IconAvatar(
            icon = TablerIcons.ArrowUp,
            background = colorGrey0(),
            tint = colorGrey400(),
            onClick = {
                if (comment.isNotEmpty()) {
                    onComment(comment)
                    comment = ""
                    focusManager.clearFocus()
                }
            }
        ).Draw()
    }
}

@Composable
private fun PostComments(viewModel: PostDetailViewModel) {
    LazyColumn(
        modifier = Modifier.heightIn(min = 0.dp, max = 10_000.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item { HorizontalDivider(thickness = 1.dp, color = colorGrey100()) }

        item {
            Text(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                text = "Comentarios",
                style = typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
                color = colorGrey900()
            )
        }

        val comments = viewModel.comments.value

        if (comments.isNotEmpty()) {
            comments.forEachIndexed { index, comment ->
                item {
                    CommentItem(comment)
                    if (index < comments.lastIndex) {
                        HorizontalDivider(thickness = 1.dp, color = colorGrey100())
                    }
                }
            }
        } else {
            item {
                Text(
                    modifier = Modifier.padding(bottom = 16.dp).fillMaxWidth(),
                    text = "Aún no hay comentarios",
                    style = typography.titleLarge,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center,
                    color = colorGrey900()
                )
            }
        }
    }
}

@Composable
private fun CommentItem(commentWithUser: CommentWithUser) {
    val user = commentWithUser.user

    Row(
        modifier = Modifier.padding(horizontal = 16.dp).height(IntrinsicSize.Min),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.Top
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
                Text(
                    modifier = Modifier.weight(1f),
                    text = commentWithUser.comment.timestamp.getTimeAgoLabel(),
                    style = typography.bodySmall,
                    textAlign = TextAlign.End,
                    fontWeight = FontWeight.Normal,
                    color = colorGrey600()
                )
            }

            Text(
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .fillMaxWidth(),
                text = commentWithUser.comment.text,
                style = typography.bodyMedium,
                fontWeight = FontWeight.Normal,
                color = colorGrey900(),
            )
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
