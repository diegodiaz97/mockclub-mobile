package com.diego.futty.home.postCreation.domain.repository

import com.diego.futty.core.domain.DataError
import com.diego.futty.core.domain.DataResult
import com.diego.futty.home.postCreation.data.network.LikedItems
import com.diego.futty.home.postCreation.domain.model.Comment
import com.diego.futty.home.postCreation.domain.model.CommentWithUser
import com.diego.futty.home.postCreation.domain.model.PostWithUser
import com.diego.futty.home.postCreation.domain.model.Tag
import dev.gitlive.firebase.firestore.Timestamp

interface PostRepository {
    suspend fun createPost(
        text: String,
        images: List<ByteArray>,
        ratio: Float,
        team: String,
        brand: String,
        tags: List<String>,
    ): DataResult<Unit, DataError.Remote>

    suspend fun countPosts(userId: String): DataResult<Int, DataError.Remote>

    /* TODO - Cuando GitLive tenga soporte de WhereIn, lo agrego */
    /*suspend fun getFeed(
        limit: Int = 20,
        startAfterTimestamp: Timestamp?,
    ): DataResult<List<PostWithUser>, DataError.Remote>*/

    suspend fun getLikedPostIdsForUser(userId: String): Set<String>

    suspend fun getFeed(
        limit: Int = 20,
        startAfterTimestamp: Timestamp?
    ): DataResult<List<PostWithUser>, DataError.Remote>

    suspend fun getPostsByUser(
        userId: String,
        limit: Int,
        startAfterTimestamp: Timestamp?
    ): DataResult<List<PostWithUser>, DataError.Remote>

    suspend fun getPostsByTeam(
        team: String,
        limit: Int = 20,
        startAfterTimestamp: Timestamp? = null
    ): DataResult<List<PostWithUser>, DataError.Remote>

    suspend fun getPostsByBrand(
        brand: String,
        limit: Int = 20,
        startAfterTimestamp: Timestamp? = null
    ): DataResult<List<PostWithUser>, DataError.Remote>

    suspend fun likePost(postId: String): DataResult<Unit, DataError.Remote>

    suspend fun removeLike(postId: String): DataResult<Unit, DataError.Remote>

    suspend fun isPostLikedByUser(postId: String): DataResult<Boolean, DataError.Remote>

    suspend fun addComment(postId: String, text: String): DataResult<CommentWithUser, DataError.Remote>

    suspend fun replyToComment(
        postId: String,
        commentId: String,
        text: String
    ): DataResult<CommentWithUser, DataError.Remote>

    suspend fun deleteComment(postId: String, commentId: String): DataResult<Unit, DataError.Remote>

    suspend fun likeCommentOrReply(
        postId: String,
        commentId: String,
        replyId: String? = null
    ): DataResult<Unit, DataError.Remote>

    suspend fun removeLikeCommentOrReply(
        postId: String,
        commentId: String,
        replyId: String?
    ): DataResult<Unit, DataError.Remote>

    suspend fun getLikedItemsForCurrentUser(postId: String): LikedItems

    suspend fun getComments(
        postId: String,
        limit: Int = 10,
        startAfterTimestamp: Timestamp? = null
    ): DataResult<List<CommentWithUser>, DataError.Remote>

    suspend fun getReplies(
        postId: String,
        commentId: String,
        limit: Int = 5,
        startAfterTimestamp: Timestamp? = null
    ): DataResult<List<CommentWithUser>, DataError.Remote>

    suspend fun deletePost(postId: String): DataResult<Unit, DataError.Remote>

    suspend fun reportPost(postId: String, reason: String): DataResult<Unit, DataError.Remote>

    suspend fun getAllTags(): DataResult<List<Tag>, DataError.Remote>

    suspend fun createOrUpdateTags(tags: List<String>): DataResult<Unit, DataError.Remote>
}
