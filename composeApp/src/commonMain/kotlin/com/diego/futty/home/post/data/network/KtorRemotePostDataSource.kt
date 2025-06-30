package com.diego.futty.home.post.data.network

import com.diego.futty.core.data.firebase.FirebaseManager
import com.diego.futty.core.data.firebase.ImageUploader
import com.diego.futty.core.data.local.UserPreferences
import com.diego.futty.core.domain.DataError
import com.diego.futty.core.domain.DataResult
import com.diego.futty.home.feed.domain.model.User
import com.diego.futty.home.post.domain.model.Comment
import com.diego.futty.home.post.domain.model.CommentWithUser
import com.diego.futty.home.post.domain.model.Post
import com.diego.futty.home.post.domain.model.PostWithUser
import dev.gitlive.firebase.firestore.Direction
import dev.gitlive.firebase.firestore.FieldValue
import kotlinx.datetime.Clock

class KtorRemotePostDataSource(
    private val firebaseManager: FirebaseManager,
    private val preferences: UserPreferences,
) : RemotePostDataSource {

    private val firestore = firebaseManager.firestore
    private val postsCollection = firestore.collection("posts")
    private val usersCollection = firestore.collection("users")
    private val storage = firebaseManager.storage
    private val storageRef = storage.reference("images/posts")

    override suspend fun createPost(
        text: String,
        images: List<ByteArray>,
        team: String,
        brand: String
    ): DataResult<Post, DataError.Remote> {
        return try {
            require(images.size <= 10) { "Máximo 10 imágenes por post" }
            val userId = preferences.getUserId() ?: ""

            usersCollection.document(userId)
                .update("postsCount" to FieldValue.increment(1))

            val imageUrls = if (images.isNotEmpty()) {
                images.mapIndexed { index, image ->
                    val imagePath = "post_images/${userId}_${
                        Clock.System.now().toEpochMilliseconds()
                    }_$index.jpg"
                    ImageUploader().uploadImage(image, imagePath)
                }
            } else {
                emptyList()
            }


            val docRef = postsCollection.document
            val post = Post(
                id = docRef.id,
                userId = userId,
                text = text,
                imageUrls = imageUrls,
                timestamp = Clock.System.now().toEpochMilliseconds(),
                team = team,
                brand = brand
            )
            docRef.set(post)

            DataResult.Success(post)
        } catch (e: Exception) {
            DataResult.Error(DataError.Remote.UNKNOWN)
        }
    }

    override suspend fun countPosts(userId: String): DataResult<Int, DataError.Remote> {
        return try {
            val result = postsCollection
                .where { "userId" equalTo userId }
                .get()
                .documents
                .size

            DataResult.Success(result)
        } catch (e: Exception) {
            DataResult.Error(DataError.Remote.UNKNOWN)
        }
    }

    override suspend fun getFeed(
        limit: Int,
        startAfterTimestamp: Long?
    ): DataResult<List<PostWithUser>, DataError.Remote> {
        return try {
            var query =
                postsCollection.orderBy("timestamp", Direction.DESCENDING).limit(limit.toLong())
            if (startAfterTimestamp != null) {
                query = query.startAfter(startAfterTimestamp)
            }
            val posts = query.get().documents.map { it.data<Post>() }

            val uniqueUserIds = posts.map { it.userId }.distinct()
            val userMap = uniqueUserIds.associateWith { getUserById(it) }
            val postsWithUser = posts.map { post -> PostWithUser(post, userMap[post.userId]) }

            DataResult.Success(postsWithUser)
        } catch (e: Exception) {
            DataResult.Error(DataError.Remote.UNKNOWN)
        }
    }

    /*
    override suspend fun getFeedFromFollowedUsers(limit: Int = 20, startAfterTimestamp: Long? = null): DataResult<List<Post>, DataError.Remote> {
        return try {
        val userId = preferences.getUserId() ?: ""
        val followedIds = firestore.collection("users").document(userId)
            .collection("following").get().documents.map { it.id }

        var query = postsCollection
            .whereIn("userId", followedIds.take(10))
            .orderBy("timestamp", descending = true)
            .limit(limit.toLong())

        if (startAfterTimestamp != null) {
            query = query.startAfter(startAfterTimestamp)
        }

        val post = query.get().documents.map { it.data<Post>() }
            DataResult.Success(post)
        } catch (e: Exception) {
            DataResult.Error(DataError.Remote.UNKNOWN)
        }
    }
    */

    override suspend fun getPostsByUser(
        userId: String,
        limit: Int,
        startAfterTimestamp: Long?
    ): DataResult<List<PostWithUser>, DataError.Remote> {
        return try {
            var query =
                postsCollection.where { "userId" equalTo userId }
                    .orderBy("timestamp", Direction.DESCENDING)
                    .limit(limit.toLong())
            if (startAfterTimestamp != null) {
                query = query.startAfter(startAfterTimestamp)
            }

            val posts = query.get().documents.map { it.data<Post>() }

            val uniqueUserIds = posts.map { it.userId }.distinct()
            val userMap = uniqueUserIds.associateWith { getUserById(it) }
            val postsWithUser = posts.map { post -> PostWithUser(post, userMap[post.userId]) }

            DataResult.Success(postsWithUser)
        } catch (e: Exception) {
            DataResult.Error(DataError.Remote.UNKNOWN)
        }
    }

    override suspend fun getPostsByTeam(
        team: String,
        limit: Int,
        startAfterTimestamp: Long?
    ): DataResult<List<PostWithUser>, DataError.Remote> {
        return try {
            var query =
                postsCollection.where { "team" equalTo team }
                    .orderBy("timestamp", Direction.DESCENDING)
                    .limit(limit.toLong())
            if (startAfterTimestamp != null) {
                query = query.startAfter(startAfterTimestamp)
            }

            val posts = query.get().documents.map { it.data<Post>() }

            val uniqueUserIds = posts.map { it.userId }.distinct()
            val userMap = uniqueUserIds.associateWith { getUserById(it) }
            val postsWithUser = posts.map { post -> PostWithUser(post, userMap[post.userId]) }

            DataResult.Success(postsWithUser)
        } catch (e: Exception) {
            DataResult.Error(DataError.Remote.UNKNOWN)
        }
    }

    override suspend fun getPostsByBrand(
        brand: String,
        limit: Int,
        startAfterTimestamp: Long?
    ): DataResult<List<PostWithUser>, DataError.Remote> {
        return try {
            var query =
                postsCollection.where { "brand" equalTo brand }
                    .orderBy("timestamp", Direction.DESCENDING)
                    .limit(limit.toLong())
            if (startAfterTimestamp != null) {
                query = query.startAfter(startAfterTimestamp)
            }

            val posts = query.get().documents.map { it.data<Post>() }

            val uniqueUserIds = posts.map { it.userId }.distinct()
            val userMap = uniqueUserIds.associateWith { getUserById(it) }
            val postsWithUser = posts.map { post -> PostWithUser(post, userMap[post.userId]) }

            DataResult.Success(postsWithUser)
        } catch (e: Exception) {
            DataResult.Error(DataError.Remote.UNKNOWN)
        }
    }

    override suspend fun likePost(postId: String): DataResult<Unit, DataError.Remote> {
        return try {
            val userId = preferences.getUserId() ?: ""
            val postRef = postsCollection.document(postId)
            val likesRef = postRef.collection("likes").document(userId)
            val dislikesRef = postRef.collection("dislikes").document(userId)

            val result = firestore.runTransaction {
                likesRef.set(mapOf("timestamp" to Clock.System.now().toEpochMilliseconds()))
                dislikesRef.delete()
                postRef.update(
                    mapOf(
                        "likesCount" to FieldValue.increment(1),
                        "dislikesCount" to FieldValue.increment(-1)
                    )
                )
            }
            DataResult.Success(result)
        } catch (e: Exception) {
            DataResult.Error(DataError.Remote.UNKNOWN)
        }
    }

    override suspend fun dislikePost(postId: String): DataResult<Unit, DataError.Remote> {
        return try {
            val userId = preferences.getUserId() ?: ""
            val postRef = postsCollection.document(postId)
            val likesRef = postRef.collection("likes").document(userId)
            val dislikesRef = postRef.collection("dislikes").document(userId)

            val result = firestore.runTransaction {
                dislikesRef.set(mapOf("timestamp" to Clock.System.now().toEpochMilliseconds()))
                likesRef.delete()
                postRef.update(
                    mapOf(
                        "dislikesCount" to FieldValue.increment(1),
                        "likesCount" to FieldValue.increment(-1)
                    )
                )
            }
            DataResult.Success(result)
        } catch (e: Exception) {
            DataResult.Error(DataError.Remote.UNKNOWN)
        }
    }

    override suspend fun removeLike(postId: String): DataResult<Unit, DataError.Remote> {
        return try {
            val userId = preferences.getUserId() ?: ""
            val postRef = postsCollection.document(postId)
            val likesRef = postRef.collection("likes").document(userId)

            val result = firestore.runTransaction {
                likesRef.delete()
                postRef.update(mapOf("likesCount" to FieldValue.increment(-1)))
            }
            DataResult.Success(result)
        } catch (e: Exception) {
            DataResult.Error(DataError.Remote.UNKNOWN)
        }
    }

    override suspend fun removeDislike(postId: String): DataResult<Unit, DataError.Remote> {
        return try {
            val userId = preferences.getUserId() ?: ""
            val postRef = postsCollection.document(postId)
            val likesRef = postRef.collection("dislikes").document(userId)

            val result = firestore.runTransaction {
                likesRef.delete()
                postRef.update(mapOf("dislikesCount" to FieldValue.increment(-1)))
            }
            DataResult.Success(result)
        } catch (e: Exception) {
            DataResult.Error(DataError.Remote.UNKNOWN)
        }
    }

    override suspend fun isPostLikedByUser(postId: String): DataResult<Boolean, DataError.Remote> {
        return try {
            val userId = preferences.getUserId() ?: ""
            val result = postsCollection.document(postId)
                .collection("likes").document(userId).get().exists
            DataResult.Success(result)
        } catch (e: Exception) {
            DataResult.Error(DataError.Remote.UNKNOWN)
        }
    }

    override suspend fun isPostDislikedByUser(postId: String): DataResult<Boolean, DataError.Remote> {
        return try {
            val userId = preferences.getUserId() ?: ""
            val result = postsCollection.document(postId)
                .collection("dislikes").document(userId).get().exists
            DataResult.Success(result)
        } catch (e: Exception) {
            DataResult.Error(DataError.Remote.UNKNOWN)
        }
    }

    override suspend fun addComment(
        postId: String,
        text: String
    ): DataResult<Comment, DataError.Remote> {
        return try {
            val userId = preferences.getUserId() ?: ""
            val commentRef = postsCollection.document(postId).collection("comments").document
            val comment = Comment(
                id = commentRef.id,
                userId = userId,
                text = text,
                timestamp = Clock.System.now().toEpochMilliseconds(),
            )

            firestore.runTransaction {
                commentRef.set(comment)
                postsCollection.document(postId)
                    .update(mapOf("commentsCount" to FieldValue.increment(1)))
            }

            DataResult.Success(comment)
        } catch (e: Exception) {
            DataResult.Error(DataError.Remote.UNKNOWN)
        }
    }

    override suspend fun replyToComment(
        postId: String,
        commentId: String,
        text: String
    ): DataResult<Comment, DataError.Remote> {
        return try {
            val userId = preferences.getUserId() ?: ""
            val replyRef = postsCollection.document(postId)
                .collection("comments").document(commentId)
                .collection("replies").document

            val reply = Comment(
                id = replyRef.id,
                userId = userId,
                text = text,
                timestamp = Clock.System.now().toEpochMilliseconds(),
            )

            replyRef.set(reply)
            DataResult.Success(reply)
        } catch (e: Exception) {
            DataResult.Error(DataError.Remote.UNKNOWN)
        }
    }

    override suspend fun deleteComment(
        postId: String,
        commentId: String
    ): DataResult<Unit, DataError.Remote> {
        return try {
            val commentRef =
                postsCollection.document(postId).collection("comments").document(commentId)

            val result = firestore.runTransaction {
                commentRef.delete()
                postsCollection.document(postId)
                    .update(mapOf("commentsCount" to FieldValue.increment(-1)))
            }
            DataResult.Success(result)
        } catch (e: Exception) {
            DataResult.Error(DataError.Remote.UNKNOWN)
        }
    }

    override suspend fun likeCommentOrReply(
        postId: String,
        commentId: String,
        replyId: String?
    ): DataResult<Unit, DataError.Remote> {
        return try {
            val userId = preferences.getUserId() ?: ""
            val target = if (replyId == null) {
                postsCollection.document(postId)
                    .collection("comments").document(commentId)
                    .collection("likes").document(userId)
            } else {
                postsCollection.document(postId)
                    .collection("comments").document(commentId)
                    .collection("replies").document(replyId)
                    .collection("likes").document(userId)
            }

            val result = target.set(mapOf("timestamp" to Clock.System.now().toEpochMilliseconds()))
            DataResult.Success(result)
        } catch (e: Exception) {
            DataResult.Error(DataError.Remote.UNKNOWN)
        }
    }

    override suspend fun getComments(
        postId: String,
        limit: Int,
        startAfterTimestamp: Long?
    ): DataResult<List<CommentWithUser>, DataError.Remote> {
        return try {
            var query = postsCollection.document(postId)
                .collection("comments")
                .orderBy("timestamp", Direction.DESCENDING)
                .limit(limit.toLong())

            if (startAfterTimestamp != null) {
                query = query.startAfter(startAfterTimestamp)
            }

            val comments = query.get().documents.map { it.data<Comment>() }

            val uniqueUserIds = comments.map { it.userId }.distinct()
            val userMap = uniqueUserIds.associateWith { getUserById(it) }
            val commentsWithUser =
                comments.map { comment -> CommentWithUser(comment, userMap[comment.userId]) }

            DataResult.Success(commentsWithUser)
        } catch (e: Exception) {
            DataResult.Error(DataError.Remote.UNKNOWN)
        }
    }

    override suspend fun getReplies(
        postId: String,
        commentId: String,
        limit: Int,
        startAfterTimestamp: Long?
    ): DataResult<List<CommentWithUser>, DataError.Remote> {
        return try {
            var query = postsCollection.document(postId)
                .collection("comments").document(commentId)
                .collection("replies")
                .orderBy("timestamp", Direction.DESCENDING)
                .limit(limit.toLong())

            if (startAfterTimestamp != null) {
                query = query.startAfter(startAfterTimestamp)
            }

            val replies = query.get().documents.map { it.data<Comment>() }

            val uniqueUserIds = replies.map { it.userId }.distinct()
            val userMap = uniqueUserIds.associateWith { getUserById(it) }
            val repliesWithUser =
                replies.map { reply -> CommentWithUser(reply, userMap[reply.userId]) }

            DataResult.Success(repliesWithUser)
        } catch (e: Exception) {
            DataResult.Error(DataError.Remote.UNKNOWN)
        }
    }

    override suspend fun deletePost(postId: String): DataResult<Unit, DataError.Remote> {
        return try {
            val userId = preferences.getUserId() ?: ""

            usersCollection.document(userId)
                .update("postsCount" to FieldValue.increment(-1))

            val postRef = postsCollection.document(postId)
            val postSnapshot = postRef.get()
            if (postSnapshot.exists) {
                val post = postSnapshot.data<Post>()

                post.imageUrls.forEach { url ->
                    val fileName = url.substringAfterLast("/")
                    storageRef.child(fileName).delete()
                }

                val result = postRef.delete()
                DataResult.Success(result)
            }
            DataResult.Success(Unit)
        } catch (e: Exception) {
            DataResult.Error(DataError.Remote.UNKNOWN)
        }
    }

    override suspend fun reportPost(
        postId: String,
        reason: String
    ): DataResult<Unit, DataError.Remote> {
        return try {
            val userId = preferences.getUserId() ?: ""
            val reportRef = postsCollection.document(postId)
                .collection("reports").document(userId)
            val result = reportRef.set(
                mapOf(
                    "reason" to reason,
                    "timestamp" to Clock.System.now().toEpochMilliseconds()
                )
            )
            DataResult.Success(result)
        } catch (e: Exception) {
            DataResult.Error(DataError.Remote.UNKNOWN)
        }
    }

    private suspend fun getUserById(userId: String): User? {
        val snapshot = usersCollection.document(userId).get()
        return if (snapshot.exists) snapshot.data<User>() else null
    }
}
