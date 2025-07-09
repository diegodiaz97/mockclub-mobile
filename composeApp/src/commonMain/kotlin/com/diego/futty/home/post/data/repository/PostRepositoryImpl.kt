package com.diego.futty.home.post.data.repository

import com.diego.futty.core.domain.DataError
import com.diego.futty.core.domain.DataResult
import com.diego.futty.home.post.data.network.RemotePostDataSource
import com.diego.futty.home.post.domain.model.Comment
import com.diego.futty.home.post.domain.model.CommentWithUser
import com.diego.futty.home.post.domain.model.PostWithUser
import com.diego.futty.home.post.domain.model.Tag
import com.diego.futty.home.post.domain.repository.PostRepository
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

    override suspend fun getLikedPostIdsForUser(userId: String): Set<String> {
        return remotePostDataSource.getLikedPostIdsForUser(userId)
    }

    override suspend fun getFeed(
        limit: Int,
        startAfterTimestamp: Timestamp?
    ): DataResult<List<PostWithUser>, DataError.Remote> {
        return remotePostDataSource.getFeed(limit, startAfterTimestamp)
    }

    override suspend fun getPostsByUser(
        userId: String,
        limit: Int,
        startAfterTimestamp: Timestamp?
    ): DataResult<List<PostWithUser>, DataError.Remote> {
        return remotePostDataSource.getPostsByUser(userId, limit, startAfterTimestamp)
    }

    override suspend fun getPostsByTeam(
        team: String,
        limit: Int,
        startAfterTimestamp: Timestamp?
    ): DataResult<List<PostWithUser>, DataError.Remote> {
        return remotePostDataSource.getPostsByTeam(team, limit, startAfterTimestamp)
    }

    override suspend fun getPostsByBrand(
        brand: String,
        limit: Int,
        startAfterTimestamp: Timestamp?
    ): DataResult<List<PostWithUser>, DataError.Remote> {
        return remotePostDataSource.getPostsByBrand(brand, limit, startAfterTimestamp)
    }

    override suspend fun likePost(postId: String): DataResult<Unit, DataError.Remote> {
        return remotePostDataSource.likePost(postId)
    }

    override suspend fun removeLike(postId: String): DataResult<Unit, DataError.Remote> {
        return remotePostDataSource.removeLike(postId)
    }

    override suspend fun isPostLikedByUser(postId: String): DataResult<Boolean, DataError.Remote> {
        return remotePostDataSource.isPostLikedByUser(postId)
    }

    override suspend fun addComment(
        postId: String,
        text: String
    ): DataResult<Comment, DataError.Remote> {
        return remotePostDataSource.addComment(postId, text)
    }

    override suspend fun replyToComment(
        postId: String,
        commentId: String,
        text: String
    ): DataResult<Comment, DataError.Remote> {
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

    override suspend fun getComments(
        postId: String,
        limit: Int,
        startAfterTimestamp: Timestamp?
    ): DataResult<List<CommentWithUser>, DataError.Remote> {
        return remotePostDataSource.getComments(postId, limit, startAfterTimestamp)
    }

    override suspend fun getReplies(
        postId: String,
        commentId: String,
        limit: Int,
        startAfterTimestamp: Timestamp?
    ): DataResult<List<CommentWithUser>, DataError.Remote> {
        return remotePostDataSource.getReplies(postId, commentId, limit, startAfterTimestamp)
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
