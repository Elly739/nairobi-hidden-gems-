package com.example.nairobihiddengems.data.repository

import com.example.nairobihiddengems.domain.models.User
import com.example.nairobihiddengems.domain.repository.UserRepository
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirestoreUserRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) : UserRepository {

    private val usersCollection = firestore.collection("users")

    override fun getUser(userId: String): Flow<User?> = callbackFlow {
        val subscription = usersCollection.document(userId).addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }
            trySend(snapshot?.toUser())
        }
        awaitClose { subscription.remove() }
    }

    override fun isFollowing(currentUserId: String, targetUserId: String): Flow<Boolean> = callbackFlow {
        val subscription = usersCollection.document(currentUserId)
            .collection("following").document(targetUserId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                trySend(snapshot?.exists() == true)
            }
        awaitClose { subscription.remove() }
    }

    override suspend fun toggleFollow(
        currentUserId: String,
        targetUserId: String,
        isFollowing: Boolean
    ): Result<Unit> {
        return try {
            val currentUserFollowingRef = usersCollection.document(currentUserId)
                .collection("following").document(targetUserId)
            val targetUserFollowerRef = usersCollection.document(targetUserId)
                .collection("followers").document(currentUserId)

            val currentUserRef = usersCollection.document(currentUserId)
            val targetUserRef = usersCollection.document(targetUserId)

            firestore.runTransaction { transaction ->
                if (isFollowing) {
                    // Follow
                    transaction.set(currentUserFollowingRef, mapOf("timestamp" to FieldValue.serverTimestamp()))
                    transaction.set(targetUserFollowerRef, mapOf("timestamp" to FieldValue.serverTimestamp()))
                    transaction.update(currentUserRef, "followingCount", FieldValue.increment(1))
                    transaction.update(targetUserRef, "followerCount", FieldValue.increment(1))
                } else {
                    // Unfollow
                    transaction.delete(currentUserFollowingRef)
                    transaction.delete(targetUserFollowerRef)
                    transaction.update(currentUserRef, "followingCount", FieldValue.increment(-1))
                    transaction.update(targetUserRef, "followerCount", FieldValue.increment(-1))
                }
            }.await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getFollowingIds(userId: String): Flow<List<String>> = callbackFlow {
        val subscription = usersCollection.document(userId)
            .collection("following")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val ids = snapshot?.documents?.map { it.id } ?: emptyList()
                trySend(ids)
            }
        awaitClose { subscription.remove() }
    }

    private fun DocumentSnapshot.toUser(): User? {
        return try {
            User(
                uid = id,
                email = getString("email") ?: "",
                displayName = getString("displayName"),
                bio = getString("bio"),
                profilePicUrl = getString("profilePicUrl"),
                coverImageUrl = getString("coverImageUrl"),
                followerCount = getLong("followerCount")?.toInt() ?: 0,
                followingCount = getLong("followingCount")?.toInt() ?: 0,
                dropsCount = getLong("dropsCount")?.toInt() ?: 0,
                savedCount = getLong("savedCount")?.toInt() ?: 0,
                isPioneer =getBoolean("isPioneer") ?: false
            )
        } catch (e: Exception) {
            null
        }
    }
}
