package com.diego.futty.home.postDetail.presentation.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.adamglin.PhosphorIcons
import com.adamglin.phosphoricons.Bold
import com.adamglin.phosphoricons.Fill
import com.adamglin.phosphoricons.bold.ArrowUp
import com.adamglin.phosphoricons.bold.ChatCircle
import com.adamglin.phosphoricons.bold.DotsThree
import com.adamglin.phosphoricons.bold.Heart
import com.adamglin.phosphoricons.bold.PaperPlaneTilt
import com.adamglin.phosphoricons.fill.Heart
import com.diego.futty.core.presentation.theme.colorError
import com.diego.futty.core.presentation.theme.colorGrey0
import com.diego.futty.core.presentation.theme.colorGrey100
import com.diego.futty.core.presentation.theme.colorGrey400
import com.diego.futty.core.presentation.theme.colorGrey600
import com.diego.futty.core.presentation.theme.colorGrey900
import com.diego.futty.core.presentation.theme.toColor
import com.diego.futty.core.presentation.utils.UserTypes.USER_TYPE_PRO
import com.diego.futty.core.presentation.utils.getShortTimeAgoLabel
import com.diego.futty.core.presentation.utils.getTimeAgoLabel
import com.diego.futty.home.design.presentation.component.avatar.Avatar
import com.diego.futty.home.design.presentation.component.avatar.AvatarSize
import com.diego.futty.home.design.presentation.component.emptystate.SimpleEmptyState
import com.diego.futty.home.design.presentation.component.image.PagerImages
import com.diego.futty.home.design.presentation.component.input.TextInput
import com.diego.futty.home.design.presentation.component.post.PostLogosWithGradient
import com.diego.futty.home.design.presentation.component.pro.VerifiedIcon
import com.diego.futty.home.design.presentation.component.topbar.TopBar
import com.diego.futty.home.design.presentation.component.topbar.TopBarActionType
import com.diego.futty.home.feed.domain.model.User
import com.diego.futty.home.feed.presentation.screen.LoadingProgress
import com.diego.futty.home.postCreation.domain.model.CommentWithExtras
import com.diego.futty.home.postCreation.domain.model.PostWithExtras
import com.diego.futty.home.postDetail.presentation.viewmodel.PostDetailViewModel

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun PostDetailScreen(
    viewModel: PostDetailViewModel,
    onUserClicked: (User) -> Unit,
    onClose: () -> Unit,
    onLiked: () -> Unit,
) {
    val postWithUser = viewModel.post.value ?: return

    BackHandler {
        onClose()
        viewModel.resetPost()
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
                title = "${postWithUser.post.team} ⨯ ${postWithUser.post.brand}",
                topBarActionType = TopBarActionType.Icon(
                    icon = PhosphorIcons.Bold.DotsThree,
                    onClick = { viewModel.onOptionsClicked() }
                ),
                onBack = {
                    onClose()
                    viewModel.resetPost()
                }
            )
        },
        content = { paddingValues ->
            Column {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(top = paddingValues.calculateTopPadding())
                        .animateContentSize()
                        .verticalScroll(rememberScrollState()),
                ) {
                    Box(Modifier.fillMaxSize()) {
                        PagerImages(
                            modifier = Modifier.fillMaxSize(),
                            aspectRatio = postWithUser.post.ratio,
                            images = postWithUser.images,
                            index = 0,
                            footer = {
                                PostLogosWithGradient(
                                    modifier = Modifier.align(Alignment.BottomCenter),
                                    teamLogo = postWithUser.post.teamLogo,
                                    brandLogo = postWithUser.post.brandLogo,
                                    designerLogo = postWithUser.post.designerLogo,
                                )
                            }
                        )
                    }
                    PostInformation(
                        post = postWithUser,
                        onLiked = {
                            viewModel.onLikeClicked()
                            onLiked()
                        },
                        onUserClicked = onUserClicked
                    )
                    if (postWithUser.post.text.isNotEmpty()) {
                        Text(
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .fillMaxWidth(),
                            text = postWithUser.post.text,
                            style = typography.bodyLarge,
                            color = colorGrey900(),
                        )
                    }
                    LoadingProgress(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                        text = "Agregando comentario",
                        progress = viewModel.commentCreationProgress.value
                    )
                    PostComments(viewModel, onUserClicked)
                }
                CommentInput(focusManager) { text -> viewModel.onCommentClicked(text) }
            }
        },
    )
    viewModel.modal.value?.Draw()
}

@Composable
fun CommentInput(
    focusManager: FocusManager,
    onComment: (String) -> Unit,
) {
    var comment by remember { mutableStateOf("") }

    Row(
        modifier = Modifier.fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(top = 8.dp, bottom = 24.dp)
            .imePadding(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        TextInput.Input(
            modifier = Modifier.weight(1f),
            input = comment,
            placeholder = "Agregar Comentario",
            background = colorGrey100(),
            onTextChangeAction = { comment = it }
        ).Draw()
        Avatar.IconAvatar(
            icon = PhosphorIcons.Bold.ArrowUp,
            background = colorGrey100(),
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
private fun PostComments(
    viewModel: PostDetailViewModel,
    onUserClicked: (User) -> Unit,
) {
    Column(
        modifier = Modifier.padding(vertical = 12.dp).animateContentSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        HorizontalDivider(thickness = 1.dp, color = colorGrey100())

        Text(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            text = "Comentarios",
            style = typography.bodyLarge,
            fontWeight = FontWeight.SemiBold,
            color = colorGrey900()
        )

        val comments = viewModel.comments.value ?: emptyList()

        if (comments.isNotEmpty()) {
            comments.forEachIndexed { index, comment ->
                CommentItem(viewModel, comment, onUserClicked)
                if (index < comments.lastIndex) {
                    HorizontalDivider(thickness = 1.dp, color = colorGrey100())
                } else {
                    HorizontalDivider(thickness = 24.dp, color = Color.Transparent)
                }
                if (index >= comments.size - 1) {
                    viewModel.getComments()
                }
            }
        } else {
            val loadedComments = viewModel.comments.value
            val showEmptyState = loadedComments != null && loadedComments.isEmpty()
            if (showEmptyState) {
                SimpleEmptyState(
                    modifier = Modifier.padding(bottom = 16.dp),
                    text = "Aún no hay comentarios",
                )
            }
        }
    }
}

@Composable
private fun CommentItem(
    viewModel: PostDetailViewModel,
    commentWithUser: CommentWithExtras,
    onUserClicked: (User) -> Unit,
) {
    val user = commentWithUser.user
    val hasLike = commentWithUser.likedByCurrentUser

    Row(
        modifier = Modifier.padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.Top
    ) {
        Avatar.ProfileAvatar(
            modifier = Modifier.padding(top = 4.dp),
            imageUrl = user.profileImage?.image,
            initials = user.profileImage?.initials,
            background = user.profileImage?.background?.toColor(),
            avatarSize = AvatarSize.Small,
            onClick = { onUserClicked(user) }
        ).Draw()
        Column(
            modifier = Modifier
                .weight(1f)
                .animateContentSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    modifier = Modifier.clickable { onUserClicked(user) },
                    text = "${user.name} ${user.lastName}",
                    style = typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = colorGrey900()
                )
                if (user.userType == USER_TYPE_PRO) {
                    VerifiedIcon(Modifier.padding(top = 2.dp), size = 16.dp)
                }
                Text(
                    modifier = Modifier.weight(1f),
                    text = " • ${commentWithUser.comment.createdAt.getShortTimeAgoLabel()}",
                    style = typography.bodySmall,
                    textAlign = TextAlign.Start,
                    color = colorGrey600()
                )
                Icon(
                    modifier = Modifier
                        .padding(start = 12.dp)
                        .clickable { /* viewModel.onMoreOptionsClicked(commentWithUser, reply) */ }
                        .size(16.dp),
                    imageVector = PhosphorIcons.Bold.DotsThree,
                    tint = colorGrey900(),
                    contentDescription = null
                )
            }

            Text(
                modifier = Modifier
                    .padding(bottom = 8.dp, end = 32.dp)
                    .fillMaxWidth(),
                text = commentWithUser.comment.text,
                style = typography.bodyMedium,
                color = colorGrey900(),
            )

            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    modifier = Modifier.clickable { viewModel.onReplyCommentClicked(commentWithUser) },
                    text = "Responder",
                    style = typography.bodySmall,
                    color = colorGrey600()
                )

                val repliesState = viewModel.repliesMap.value[commentWithUser.comment.id]
                val repliesVisible = repliesState?.visible ?: false
                val replies = commentWithUser.replies

                if (replies.size < commentWithUser.replyCount && !repliesVisible) {
                    val repliesText =
                        if (commentWithUser.replyCount - replies.size == 1) "respuesta" else "respuestas"
                    Text(
                        modifier = Modifier.clickable {
                            viewModel.onShowRepliesClicked(commentWithUser)
                        },
                        text = "Ver ${commentWithUser.replyCount} $repliesText",
                        style = typography.bodySmall,
                        textAlign = TextAlign.End,
                        color = colorGrey600()
                    )
                }

                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End,
                ) {
                    Icon(
                        modifier = Modifier
                            .clickable { viewModel.onLikeCommentClicked(commentWithUser) }
                            .padding(end = 4.dp)
                            .size(16.dp),
                        imageVector = if (hasLike) PhosphorIcons.Fill.Heart else PhosphorIcons.Bold.Heart,
                        tint = if (hasLike) colorError() else colorGrey900(),
                        contentDescription = null
                    )
                    Text(
                        text = commentWithUser.likeCount.toString(),
                        style = typography.bodySmall,
                        color = if (hasLike) colorError() else colorGrey900(),
                    )
                }
            }
            val density = LocalDensity.current
            val repliesState = viewModel.repliesMap.value[commentWithUser.comment.id]
            val replies = repliesState?.replies ?: emptyList()
            val showReplies = repliesState?.visible == true
            AnimatedVisibility(
                visible = showReplies,
                enter = slideInVertically { with(density) { -40.dp.roundToPx() } }
                        + expandVertically(expandFrom = Alignment.Top)
                        + fadeIn(initialAlpha = 0.3f),
                exit = slideOutVertically()
                        + shrinkVertically()
                        + fadeOut()
            ) {
                if (replies.isNotEmpty()) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        replies.forEach { reply ->
                            val replyUser = reply.user
                            val liked = reply.likedByCurrentUser
                            Row(
                                modifier = Modifier.padding(vertical = 8.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                Avatar.ProfileAvatar(
                                    modifier = Modifier.padding(top = 4.dp),
                                    imageUrl = replyUser.profileImage?.image,
                                    initials = replyUser.profileImage?.initials,
                                    background = replyUser.profileImage?.background?.toColor(),
                                    avatarSize = AvatarSize.Small,
                                    onClick = { onUserClicked(replyUser) }
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
                                            modifier = Modifier.clickable { onUserClicked(replyUser) },
                                            text = "${replyUser.name} ${replyUser.lastName}",
                                            style = typography.bodyMedium,
                                            fontWeight = FontWeight.SemiBold,
                                            color = colorGrey900()
                                        )
                                        if (replyUser.userType == USER_TYPE_PRO) {
                                            VerifiedIcon(Modifier.padding(top = 2.dp), size = 16.dp)
                                        }
                                        Text(
                                            modifier = Modifier.weight(1f),
                                            text = " • ${reply.comment.createdAt.getShortTimeAgoLabel()}",
                                            style = typography.bodySmall,
                                            color = colorGrey600()
                                        )
                                        Icon(
                                            modifier = Modifier
                                                .padding(start = 12.dp)
                                                .clickable { /* viewModel.onMoreOptionsClicked(commentWithUser, reply) */ }
                                                .size(16.dp),
                                            imageVector = PhosphorIcons.Bold.DotsThree,
                                            tint = colorGrey900(),
                                            contentDescription = null
                                        )
                                    }

                                    Row(
                                        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        verticalAlignment = Alignment.Bottom,
                                    ) {
                                        Text(
                                            modifier = Modifier.weight(1f),
                                            text = reply.comment.text,
                                            style = typography.bodyMedium,
                                            color = colorGrey900(),
                                        )

                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                                        ) {
                                            Icon(
                                                modifier = Modifier
                                                    .clickable {
                                                        viewModel.onLikeCommentClicked(
                                                            commentWithUser,
                                                            reply
                                                        )
                                                    }
                                                    .size(16.dp),
                                                imageVector = if (liked) PhosphorIcons.Fill.Heart else PhosphorIcons.Bold.Heart,
                                                tint = if (liked) colorError() else colorGrey900(),
                                                contentDescription = null
                                            )
                                            Text(
                                                text = reply.likeCount.toString(),
                                                style = typography.bodySmall,
                                                color = if (liked) colorError() else colorGrey900(),
                                            )
                                        }
                                    }
                                }
                            }
                        }
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                modifier = Modifier.clickable {
                                    viewModel.onHideRepliesClicked(
                                        commentWithUser
                                    )
                                },
                                text = "Ocultar respuestas",
                                style = typography.bodySmall,
                                color = colorGrey600(),
                            )
                            if (replies.size < commentWithUser.replyCount) {
                                Text(
                                    modifier = Modifier.clickable {
                                        viewModel.onShowRepliesClicked(
                                            commentWithUser
                                        )
                                    },
                                    text = "Ver más respuestas",
                                    style = typography.bodySmall,
                                    color = colorGrey600(),
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PostInformation(
    post: PostWithExtras,
    onLiked: () -> Unit,
    onUserClicked: (User) -> Unit,
) {
    val user = post.user
    Row(
        modifier = Modifier.background(colorGrey0()).padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Avatar.ProfileAvatar(
            imageUrl = user.profileImage?.image,
            initials = user.profileImage?.initials,
            background = user.profileImage?.background?.toColor(),
            avatarSize = AvatarSize.Small,
            onClick = { onUserClicked(user) }
        ).Draw()
        Column(
            modifier = Modifier
                .weight(1f)
                .clickable { onUserClicked(user) }
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = "${user.name} ${user.lastName}",
                    style = typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = colorGrey900()
                )
                if (user.userType == USER_TYPE_PRO) {
                    VerifiedIcon(Modifier.padding(top = 4.dp), size = 16.dp)
                }
            }
            Text(
                text = post.post.createdAt.getTimeAgoLabel(),
                style = typography.bodySmall,
                color = colorGrey600()
            )
        }

        PostInteractions(
            post = post,
            onLiked = onLiked,
        )
    }
}

@Composable
private fun PostInteractions(
    post: PostWithExtras,
    onLiked: () -> Unit,
) {
    val hasLike = post.likedByCurrentUser
    Row(
        modifier = Modifier.padding(bottom = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(1.dp)
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
            fontWeight = FontWeight.SemiBold,
            color = if (hasLike) colorError() else colorGrey900(),
        )

        Spacer(Modifier.width(6.dp))

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
            fontWeight = FontWeight.SemiBold,
            color = colorGrey900()
        )

        Spacer(Modifier.width(6.dp))

        Avatar.IconAvatar(
            icon = PhosphorIcons.Bold.PaperPlaneTilt,
            tint = colorGrey900(),
            background = colorGrey0(),
            avatarSize = AvatarSize.Small,
            onClick = { /*onShare()*/ }
        ).Draw()
    }
}
