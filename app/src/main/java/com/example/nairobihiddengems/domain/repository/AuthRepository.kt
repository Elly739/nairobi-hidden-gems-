package com.example.nairobihiddengems.domain.repository

import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.StateFlow

interface AuthRepository {
    val currentUser: StateFlow<FirebaseUser?>
    
    suspend fun signIn(email: String, password: String): Result<FirebaseUser?>
    suspend fun signUp(email: String, password: String): Result<FirebaseUser?>
    fun signOut()
    fun isUserAuthenticated(): Boolean
    suspend fun updateProfile(displayName: String): Result<Unit>
}
