package com.example.nairobihiddengems.ui.add

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nairobihiddengems.domain.models.Place
import com.example.nairobihiddengems.domain.repository.AuthRepository
import com.example.nairobihiddengems.domain.repository.PlaceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddGemViewModel @Inject constructor(
    private val repository: PlaceRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<AddGemUiState>(AddGemUiState.Idle)
    val uiState = _uiState.asStateFlow()

    private val _selectedImageUri = MutableStateFlow<Uri?>(null)
    val selectedImageUri = _selectedImageUri.asStateFlow()

    fun onImageSelected(uri: Uri?) {
        _selectedImageUri.value = uri
    }

    fun postGem(
        name: String,
        location: String,
        category: String,
        description: String,
        aesthetic: Double,
        chill: Double,
        crowd: Double
    ) {
        val user = authRepository.currentUser.value ?: return
        
        viewModelScope.launch {
            _uiState.value = AddGemUiState.Loading
            
            val newPlace = Place(
                id = "", // Firestore will generate
                name = name,
                location = location,
                category = category,
                vibe = listOf("Chill", "Aesthetic"), // Default vibes for now
                saves = 0,
                rating = (aesthetic + chill + crowd) / 3.0,
                description = description,
                imageUrl = _selectedImageUri.value?.toString() ?: "https://images.unsplash.com/photo-1514933651103-005eec06c04b",
                createdBy = user.uid
            )

            try {
                repository.addPlace(newPlace)
                _uiState.value = AddGemUiState.Success
            } catch (e: Exception) {
                _uiState.value = AddGemUiState.Error(e.message ?: "Failed to post gem")
            }
        }
    }

    fun resetState() {
        _uiState.value = AddGemUiState.Idle
        _selectedImageUri.value = null
    }
}

sealed class AddGemUiState {
    object Idle : AddGemUiState()
    object Loading : AddGemUiState()
    object Success : AddGemUiState()
    data class Error(val message: String) : AddGemUiState()
}
