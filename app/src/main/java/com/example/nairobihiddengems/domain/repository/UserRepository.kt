package com.example.nairobihiddengems.domain.repository

import com.example.nairobihiddengems.domain.models.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getUser(userId: String): Flow<User?>
    fun isFollowing(currentUserId: String, targetUserId: String): Flow<Boolean>
    suspend fun toggleFollow(currentUserId: String, targetUserId: String, isFollowing: Boolean): Result<Unit>
    fun getFollowingIds(userId: String): Flow<List<String>>
}
