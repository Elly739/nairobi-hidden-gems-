package com.example.nairobihiddengems.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import android.util.Log
import com.example.nairobihiddengems.data.repository.FirestorePlaceRepository
import com.example.nairobihiddengems.domain.models.Place
import com.example.nairobihiddengems.domain.repository.PlaceRepository
import com.example.nairobihiddengems.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: PlaceRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val TAG = "HomeViewModel"

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _selectedVibe = MutableStateFlow("All Vibes")
    val selectedVibe = _selectedVibe.asStateFlow()

    private val _isSeeding = MutableStateFlow(false)
    val isSeeding = _isSeeding.asStateFlow()

    @OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
    val savedPlaceIds: StateFlow<Set<String>> = authRepository.currentUser
        .flatMapLatest { user ->
            if (user != null) {
                repository.getSavedPlaces(user.uid).map { it.map { p -> p.id }.toSet() }
            } else {
                flowOf(emptySet())
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptySet())

    val trendingPlaces: StateFlow<List<Place>> = repository.getTrendingPlaces()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _places = repository.getPlaces()
    
    val filteredPlaces: StateFlow<List<Place>> = combine(_places, _searchQuery, _selectedVibe) { places, query, vibe ->
        places.filter { place ->
            val matchesQuery = place.name.contains(query, ignoreCase = true) || 
                              place.location.contains(query, ignoreCase = true)
            val matchesVibe = vibe == "All Vibes" || place.category.contains(vibe.split(" ").last(), ignoreCase = true)
            matchesQuery && matchesVibe
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun onSearchQueryChange(newQuery: String) {
        _searchQuery.value = newQuery
    }

    fun onVibeSelect(vibe: String) {
        _selectedVibe.value = vibe
    }

    fun toggleSave(placeId: String) {
        val user = authRepository.currentUser.value ?: return
        val isCurrentlySaved = savedPlaceIds.value.contains(placeId)
        viewModelScope.launch {
            repository.toggleSavePlace(user.uid, placeId, !isCurrentlySaved)
        }
    }

    fun seedData() {
        if (_isSeeding.value) return
        viewModelScope.launch {
            if (!authRepository.isUserAuthenticated()) {
                Log.e(TAG, "Cannot seed data: User is not authenticated!")
                return@launch
            }
            _isSeeding.value = true
            Log.d(TAG, "Starting data seeding...")
            val firestoreRepo = repository as? FirestorePlaceRepository
            if (firestoreRepo == null) {
                Log.e(TAG, "Repository is not FirestorePlaceRepository!")
                _isSeeding.value = false
                return@launch
            }
            
            try {
                val seedPlaces = listOf(
                    Place(
                        "1", "Kiza Rooftop Lounge", "Westlands", "Night", 
                        listOf("Chill", "Music", "Rooftop"), 312, 4.9,
                        "The ultimate rooftop experience in Nairobi.",
                        "https://images.unsplash.com/photo-1514933651103-005eec06c04b?auto=format&fit=crop&w=800&q=80"
                    ),
                    Place(
                        "2", "The Java House Garden", "Kilimani", "Cafe", 
                        listOf("Study", "Quiet", "Garden"), 156, 4.6,
                        "A serene spot for your morning coffee.",
                        "https://images.unsplash.com/photo-1554118811-1e0d58224f24?auto=format&fit=crop&w=800&q=80"
                    ),
                    Place(
                        "3", "Ngong Road Sunset Point", "Ngong Road", "Sunset", 
                        listOf("Sunset", "Views", "Outdoor"), 524, 4.8,
                        "Best sunset view in the city.",
                        "https://images.unsplash.com/photo-1441974231531-c6227db76b6e?auto=format&fit=crop&w=800&q=80"
                    )
                )
                seedPlaces.forEach { 
                    Log.d(TAG, "Seeding: ${it.name}")
                    firestoreRepo.addPlace(it) 
                }
                Log.d(TAG, "Data seeding completed successfully!")
            } catch (e: Exception) {
                Log.e(TAG, "Data seeding failed: ${e.message}", e)
            } finally {
                _isSeeding.value = false
            }
        }
    }
}
