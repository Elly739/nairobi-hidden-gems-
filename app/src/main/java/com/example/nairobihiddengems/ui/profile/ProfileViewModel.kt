package com.example.nairobihiddengems.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nairobihiddengems.domain.models.User
import com.example.nairobihiddengems.domain.repository.AuthRepository
import com.example.nairobihiddengems.domain.repository.PlaceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val placeRepository: PlaceRepository
) : ViewModel() {

    val user: StateFlow<User?> = authRepository.currentUser

    @OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
    val savedCount: StateFlow<Int> = authRepository.currentUser
        .flatMapLatest { user ->
            if (user != null) {
                placeRepository.getSavedPlaces(user.uid).map { it.size }
            } else {
                flowOf(0)
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    @OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
    val dropsCount: StateFlow<Int> = authRepository.currentUser
        .flatMapLatest { user ->
            if (user != null) {
                placeRepository.getPlacesByUser(user.uid).map { it.size }
            } else {
                flowOf(0)
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    fun updateDisplayName(name: String, bio: String? = null) {
        viewModelScope.launch {
            authRepository.updateProfile(name, bio = bio)
        }
    }

    fun signOut() {
        authRepository.signOut()
    }
}
