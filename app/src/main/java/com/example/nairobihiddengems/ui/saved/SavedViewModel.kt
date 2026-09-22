package com.example.nairobihiddengems.ui.saved

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nairobihiddengems.domain.models.Place
import com.example.nairobihiddengems.domain.repository.AuthRepository
import com.example.nairobihiddengems.domain.repository.PlaceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class SavedViewModel @Inject constructor(
    private val placeRepository: PlaceRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    @OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
    val savedPlaces: StateFlow<List<Place>> = authRepository.currentUser
        .flatMapLatest { user ->
            if (user != null) {
                placeRepository.getSavedPlaces(user.uid)
            } else {
                flowOf(emptyList())
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}
