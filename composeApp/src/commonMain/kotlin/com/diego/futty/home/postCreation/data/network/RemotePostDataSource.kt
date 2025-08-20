package com.diego.futty.home.postCreation.data.network

import com.diego.futty.core.domain.DataError
import com.diego.futty.core.domain.DataResult
import com.diego.futty.home.feed.domain.model.Count
import com.diego.futty.home.postCreation.domain.model.CommentWithExtras
import com.diego.futty.home.postCreation.domain.model.PostWithExtras
import com.diego.futty.home.postCreation.domain.model.Tag
import dev.gitlive.firebase.firestore.Timestamp

interface RemotePostDataSource {
    suspend fun createPost(
        text: String,
        images: List<ByteArray>,
        ratio: Float,
        team: String,
        brand: String,
        teamLogo: ByteArray,
        brandLogo: ByteArray,
        designerLogo: ByteArray?,
        tags: List<String>,
    ): DataResult<Unit, DataError.Remote>

    suspend fun countPosts(userId: String): DataResult<Count, DataError.Remote>

    /* TODO - Cuando GitLive tenga soporte de WhereIn, lo agrego */
    /*suspend fun getFeed(
        limit: Int = 20,
        startAfterTimestamp: Timestamp?,
    ): DataResult<List<PostWithUser>, DataError.Remote>*/

    suspend fun getFeed(
        limit: Int = 10,
        cursorCreatedAt: Long? = null
    ): DataResult<List<PostWithExtras>, DataError.Remote>

    suspend fun getPostsByUser(
        userId: String,
        limit: Int = 10,
        cursorCreatedAt: Long? = null
    ): DataResult<List<PostWithExtras>, DataError.Remote>

    suspend fun getPostsByTeam(
        team: String,
        limit: Int = 20,
        startAfterTimestamp: Timestamp? = null
    ): DataResult<List<PostWithExtras>, DataError.Remote>

    suspend fun getPostsByBrand(
        brand: String,
        limit: Int = 20,
        startAfterTimestamp: Timestamp? = null
    ): DataResult<List<PostWithExtras>, DataError.Remote>

    suspend fun likePost(postId: String): DataResult<Unit, DataError.Remote>

    suspend fun unlikePost(postId: String): DataResult<Unit, DataError.Remote>

    suspend fun addComment(
        postId: String,
        text: String,
        parentCommentId: String? = null
    ): DataResult<CommentWithExtras, DataError.Remote>

    suspend fun replyToComment(
        postId: String,
        commentId: String,
        text: String
    ): DataResult<CommentWithExtras, DataError.Remote>

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
        cursorCreatedAt: Long? = null
    ): DataResult<List<CommentWithExtras>, DataError.Remote>

    suspend fun getReplies(
        commentId: String,
        limit: Int = 5,
        cursorCreatedAt: Long? = null
    ): DataResult<List<CommentWithExtras>, DataError.Remote>

    suspend fun deletePost(postId: String): DataResult<Unit, DataError.Remote>

    suspend fun reportPost(postId: String, reason: String): DataResult<Unit, DataError.Remote>

    suspend fun getAllTags(): DataResult<List<Tag>, DataError.Remote>

    suspend fun createOrUpdateTags(tags: List<String>): DataResult<Unit, DataError.Remote>
}
