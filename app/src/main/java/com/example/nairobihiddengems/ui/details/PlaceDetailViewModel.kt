package com.example.nairobihiddengems.ui.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nairobihiddengems.domain.models.Place
import com.example.nairobihiddengems.domain.repository.AuthRepository
import com.example.nairobihiddengems.domain.repository.PlaceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlaceDetailViewModel @Inject constructor(
    private val repository: PlaceRepository,
    private val authRepository: AuthRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val placeId: String = checkNotNull(savedStateHandle["placeId"])

    val place: StateFlow<Place?> = repository.getPlaceById(placeId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    @OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
    val isSaved: StateFlow<Boolean> = authRepository.currentUser
        .flatMapLatest { user ->
            if (user != null) {
                repository.isPlaceSaved(user.uid, placeId)
            } else {
                flowOf(false)
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    fun toggleSave() {
        val user = authRepository.currentUser.value ?: return
        val currentSaved = isSaved.value
        viewModelScope.launch {
            repository.toggleSavePlace(user.uid, placeId, !currentSaved)
        }
    }
}
