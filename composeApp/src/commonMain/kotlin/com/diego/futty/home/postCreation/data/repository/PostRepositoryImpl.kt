package com.diego.futty.home.postCreation.data.repository

import com.diego.futty.core.domain.DataError
import com.diego.futty.core.domain.DataResult
import com.diego.futty.home.postCreation.data.network.LikedItems
import com.diego.futty.home.postCreation.data.network.RemotePostDataSource
import com.diego.futty.home.postCreation.domain.model.CommentWithExtras
import com.diego.futty.home.postCreation.domain.model.PostWithExtras
import com.diego.futty.home.postCreation.domain.model.Tag
import com.diego.futty.home.postCreation.domain.repository.PostRepository
import dev.gitlive.firebase.firestore.Timestamp

class PostRepositoryImpl(
    private val remotePostDataSource: RemotePostDataSource
) : PostRepository {
    override suspend fun createPost(
        text: String,
        images: List<ByteArray>,
        ratio: Float,
        team: String,
        brand: String,
        tags: List<String>,
    ): DataResult<Unit, DataError.Remote> {
        return remotePostDataSource.createPost(text, images, ratio, team, brand, tags)
    }

    override suspend fun countPosts(userId: String): DataResult<Int, DataError.Remote> {
        return remotePostDataSource.countPosts(userId)
    }

    override suspend fun getFeed(
        limit: Int,
        cursorCreatedAt: Long?
    ): DataResult<List<PostWithExtras>, DataError.Remote> {
        return remotePostDataSource.getFeed(limit, cursorCreatedAt)
    }

    override suspend fun getPostsByUser(
        userId: String,
        limit: Int,
        cursorCreatedAt: Long?
    ): DataResult<List<PostWithExtras>, DataError.Remote> {
        return remotePostDataSource.getPostsByUser(userId, limit, cursorCreatedAt)
    }

    override suspend fun getPostsByTeam(
        team: String,
        limit: Int,
        startAfterTimestamp: Timestamp?
    ): DataResult<List<PostWithExtras>, DataError.Remote> {
        return remotePostDataSource.getPostsByTeam(team, limit, startAfterTimestamp)
    }

    override suspend fun getPostsByBrand(
        brand: String,
        limit: Int,
        startAfterTimestamp: Timestamp?
    ): DataResult<List<PostWithExtras>, DataError.Remote> {
        return remotePostDataSource.getPostsByBrand(brand, limit, startAfterTimestamp)
    }

    override suspend fun likePost(postId: String): DataResult<Unit, DataError.Remote> {
        return remotePostDataSource.likePost(postId)
    }

    override suspend fun unlikePost(postId: String): DataResult<Unit, DataError.Remote> {
        return remotePostDataSource.unlikePost(postId)
    }

    override suspend fun addComment(
        postId: String,
        text: String,
        parentCommentId: String?,
    ): DataResult<CommentWithExtras, DataError.Remote> {
        return remotePostDataSource.addComment(postId, text, parentCommentId)
    }

    override suspend fun replyToComment(
        postId: String,
        commentId: String,
        text: String
    ): DataResult<CommentWithExtras, DataError.Remote> {
        return remotePostDataSource.replyToComment(postId, commentId, text)
    }

    override suspend fun deleteComment(
        postId: String,
        commentId: String
    ): DataResult<Unit, DataError.Remote> {
        return remotePostDataSource.deleteComment(postId, commentId)
    }

    override suspend fun likeCommentOrReply(
        postId: String,
        commentId: String,
        replyId: String?
    ): DataResult<Unit, DataError.Remote> {
        return remotePostDataSource.likeCommentOrReply(postId, commentId, replyId)
    }

    override suspend fun removeLikeCommentOrReply(
        postId: String,
        commentId: String,
        replyId: String?
    ): DataResult<Unit, DataError.Remote> {
        return remotePostDataSource.removeLikeCommentOrReply(postId, commentId, replyId)
    }

    override suspend fun getLikedItemsForCurrentUser(postId: String): LikedItems {
        return remotePostDataSource.getLikedItemsForCurrentUser(postId)
    }

    override suspend fun getComments(
        postId: String,
        limit: Int,
        cursorCreatedAt: Long?
    ): DataResult<List<CommentWithExtras>, DataError.Remote> {
        return remotePostDataSource.getComments(postId, limit, cursorCreatedAt)
    }

    override suspend fun getReplies(
        commentId: String,
        limit: Int,
        cursorCreatedAt: Long?
    ): DataResult<List<CommentWithExtras>, DataError.Remote> {
        return remotePostDataSource.getReplies(commentId, limit, cursorCreatedAt)
    }

    override suspend fun deletePost(postId: String): DataResult<Unit, DataError.Remote> {
        return remotePostDataSource.deletePost(postId)
    }

    override suspend fun reportPost(
        postId: String,
        reason: String
    ): DataResult<Unit, DataError.Remote> {
        return remotePostDataSource.reportPost(postId, reason)
    }

    override suspend fun getAllTags(): DataResult<List<Tag>, DataError.Remote> {
        return remotePostDataSource.getAllTags()
    }

    override suspend fun createOrUpdateTags(tags: List<String>): DataResult<Unit, DataError.Remote> {
        return remotePostDataSource.createOrUpdateTags(tags)
    }
}
