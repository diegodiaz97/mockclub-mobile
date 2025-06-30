package com.diego.futty.home.post.data.network

import com.diego.futty.core.domain.DataError
import com.diego.futty.core.domain.DataResult
import com.diego.futty.home.post.domain.model.Comment
import com.diego.futty.home.post.domain.model.CommentWithUser
import com.diego.futty.home.post.domain.model.Post
import com.diego.futty.home.post.domain.model.PostWithUser

interface RemotePostDataSource {
    suspend fun createPost(
        text: String,
        images: List<ByteArray>,
        team: String,
        brand: String
    ): DataResult<Post, DataError.Remote>

    suspend fun countPosts(userId: String): DataResult<Int, DataError.Remote>

    /* TODO - Cuando GitLive tenga soporte de WhereIn, lo agrego */
    /*suspend fun getFeed(
        limit: Int = 20,
        startAfterTimestamp: Long?,
    ): DataResult<List<PostWithUser>, DataError.Remote>*/

    suspend fun getFeed(
        limit: Int = 20,
        startAfterTimestamp: Long?
    ): DataResult<List<PostWithUser>, DataError.Remote>

    suspend fun getPostsByUser(
        userId: String,
        limit: Int,
        startAfterTimestamp: Long?
    ): DataResult<List<PostWithUser>, DataError.Remote>

    suspend fun getPostsByTeam(
        team: String,
        limit: Int = 20,
        startAfterTimestamp: Long? = null
    ): DataResult<List<PostWithUser>, DataError.Remote>

    suspend fun getPostsByBrand(
        brand: String,
        limit: Int = 20,
        startAfterTimestamp: Long? = null
    ): DataResult<List<PostWithUser>, DataError.Remote>

    suspend fun likePost(postId: String): DataResult<Unit, DataError.Remote>

    suspend fun dislikePost(postId: String): DataResult<Unit, DataError.Remote>

    suspend fun removeLike(postId: String): DataResult<Unit, DataError.Remote>

    suspend fun removeDislike(postId: String): DataResult<Unit, DataError.Remote>

    suspend fun isPostLikedByUser(postId: String): DataResult<Boolean, DataError.Remote>

    suspend fun isPostDislikedByUser(postId: String): DataResult<Boolean, DataError.Remote>

    suspend fun addComment(postId: String, text: String): DataResult<Comment, DataError.Remote>

    suspend fun replyToComment(
        postId: String,
        commentId: String,
        text: String
    ): DataResult<Comment, DataError.Remote>

    suspend fun deleteComment(postId: String, commentId: String): DataResult<Unit, DataError.Remote>

    suspend fun likeCommentOrReply(
        postId: String,
        commentId: String,
        replyId: String? = null
    ): DataResult<Unit, DataError.Remote>

    suspend fun getComments(
        postId: String,
        limit: Int = 20,
        startAfterTimestamp: Long? = null
    ): DataResult<List<CommentWithUser>, DataError.Remote>

    suspend fun getReplies(
        postId: String,
        commentId: String,
        limit: Int = 20,
        startAfterTimestamp: Long? = null
    ): DataResult<List<CommentWithUser>, DataError.Remote>

    suspend fun deletePost(postId: String): DataResult<Unit, DataError.Remote>

    suspend fun reportPost(postId: String, reason: String): DataResult<Unit, DataError.Remote>
}
