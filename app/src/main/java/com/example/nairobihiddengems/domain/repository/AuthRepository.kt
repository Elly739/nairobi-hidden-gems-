package com.example.nairobihiddengems.domain.repository

import com.example.nairobihiddengems.domain.models.User
import kotlinx.coroutines.flow.StateFlow

interface AuthRepository {
    val currentUser: StateFlow<User?>
    
    suspend fun signIn(email: String, password: String): Result<User?>
    suspend fun signUp(email: String, password: String, name: String): Result<User?>
    fun signOut()
    fun isUserAuthenticated(): Boolean
    suspend fun updateProfile(displayName: String, bio: String? = null, profilePicUrl: String? = null, coverImageUrl: String? = null): Result<Unit>
}
