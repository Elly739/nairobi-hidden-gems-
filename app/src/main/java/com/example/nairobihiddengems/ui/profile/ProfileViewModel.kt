package com.example.nairobihiddengems.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nairobihiddengems.domain.models.Place
import com.example.nairobihiddengems.domain.models.User
import com.example.nairobihiddengems.domain.repository.AuthRepository
import com.example.nairobihiddengems.domain.repository.PlaceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val placeRepository: PlaceRepository
) : ViewModel() {

    val user: StateFlow<User?> = authRepository.currentUser

    @OptIn(ExperimentalCoroutinesApi::class)
    val savedPlaces: StateFlow<List<Place>> = authRepository.currentUser
        .flatMapLatest { user ->
            if (user != null) placeRepository.getSavedPlaces(user.uid)
            else flowOf(emptyList())
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    @OptIn(ExperimentalCoroutinesApi::class)
    val postedPlaces: StateFlow<List<Place>> = authRepository.currentUser
        .flatMapLatest { user ->
            if (user != null) placeRepository.getPlacesByUser(user.uid)
            else flowOf(emptyList())
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    @OptIn(ExperimentalCoroutinesApi::class)
    val repostedPlaces: StateFlow<List<Place>> = authRepository.currentUser
        .flatMapLatest { user ->
            if (user != null) placeRepository.getRepostedPlaces(user.uid)
            else flowOf(emptyList())
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val savedCount: StateFlow<Int> = savedPlaces.map { it.size }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val dropsCount: StateFlow<Int> = postedPlaces.map { it.size }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val repostsCount: StateFlow<Int> = repostedPlaces.map { it.size }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    fun updateProfile(name: String, bio: String? = null, profilePicUrl: String? = null, coverImageUrl: String? = null) {
        viewModelScope.launch {
            authRepository.updateProfile(name, bio = bio, profilePicUrl = profilePicUrl, coverImageUrl = coverImageUrl)
        }
    }

    fun signOut() {
        authRepository.signOut()
    }
}
