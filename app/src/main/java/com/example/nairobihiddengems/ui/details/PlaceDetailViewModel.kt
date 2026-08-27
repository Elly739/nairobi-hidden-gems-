package com.example.nairobihiddengems.ui.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nairobihiddengems.domain.models.Place
import com.example.nairobihiddengems.domain.repository.PlaceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class PlaceDetailViewModel @Inject constructor(
    repository: PlaceRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val placeId: String = checkNotNull(savedStateHandle["placeId"])

    val place: StateFlow<Place?> = repository.getPlaceById(placeId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)
}
