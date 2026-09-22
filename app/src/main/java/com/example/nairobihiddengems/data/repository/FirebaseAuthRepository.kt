package com.example.nairobihiddengems.data.repository

import com.example.nairobihiddengems.domain.models.User
import com.example.nairobihiddengems.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseAuthRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : AuthRepository {

    private val _currentUser = MutableStateFlow<User?>(null)
    override val currentUser: StateFlow<User?> = _currentUser.asStateFlow()
    
    private var userListener: ListenerRegistration? = null

    init {
        firebaseAuth.addAuthStateListener { auth ->
            val firebaseUser = auth.currentUser
            if (firebaseUser != null) {
                listenToUser(firebaseUser.uid)
            } else {
                userListener?.remove()
                _currentUser.value = null
            }
        }
    }

    private fun listenToUser(uid: String) {
        userListener?.remove()
        userListener = firestore.collection("users").document(uid)
            .addSnapshotListener { snapshot, error ->
                if (snapshot != null && snapshot.exists()) {
                    _currentUser.value = snapshot.toObject(User::class.java)?.copy(uid = uid)
                } else {
                    // Fallback to basic auth info if doc doesn't exist yet
                    val firebaseUser = firebaseAuth.currentUser
                    if (firebaseUser != null) {
                        _currentUser.value = User(
                            uid = firebaseUser.uid,
                            email = firebaseUser.email ?: "",
                            displayName = firebaseUser.displayName
                        )
                    }
                }
            }
    }

    override suspend fun signIn(email: String, password: String): Result<User?> {
        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            val user = firestore.collection("users").document(result.user!!.uid).get().await().toObject(User::class.java)
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun signUp(email: String, password: String, name: String): Result<User?> {
        return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val newUser = User(
                uid = result.user!!.uid,
                email = email,
                displayName = name,
                followerCount = 0,
                dropsCount = 0,
                isPioneer = true
            )
            firestore.collection("users").document(newUser.uid).set(newUser).await()
            Result.success(newUser)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun signOut() {
        userListener?.remove()
        firebaseAuth.signOut()
    }

    override fun isUserAuthenticated(): Boolean {
        return firebaseAuth.currentUser != null
    }

    override suspend fun updateProfile(
        displayName: String,
        bio: String?,
        profilePicUrl: String?,
        coverImageUrl: String?
    ): Result<Unit> {
        return try {
            val uid = firebaseAuth.currentUser?.uid ?: throw Exception("Not logged in")
            val updates = mutableMapOf<String, Any>(
                "displayName" to displayName
            )
            bio?.let { updates["bio"] = it }
            profilePicUrl?.let { updates["profilePicUrl"] = it }
            coverImageUrl?.let { updates["coverImageUrl"] = it }
            
            firestore.collection("users").document(uid).update(updates).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
