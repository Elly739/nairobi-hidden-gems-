package com.example.nairobihiddengems.data.repository

import com.example.nairobihiddengems.domain.models.Place
import com.example.nairobihiddengems.domain.repository.PlaceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MockPlaceRepository @Inject constructor() : PlaceRepository {
    private val mockPlaces = listOf(
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
        ),
        Place(
            "4", "Artcaffe Market", "Westlands", "Cafe", 
            listOf("Aesthetic", "Study", "Grind"), 289, 4.5,
            "Study and grind at this aesthetic market cafe.",
            "https://images.unsplash.com/photo-1582719508461-905c673771fd?auto=format&fit=crop&w=800&q=80"
        ),
        Place(
            "5", "Karura Forest", "Limuru Road", "Nature", 
            listOf("Nature", "Quiet", "Active"), 890, 4.7,
            "Escape the city in this lush forest.",
            "https://images.unsplash.com/photo-1448375240586-882707db888b?auto=format&fit=crop&w=800&q=80"
        ),
        Place(
            "6", "The Alchemist Bar", "Westlands", "Night", 
            listOf("Music", "Art", "Crowd"), 645, 4.4,
            "Creative space with food, drinks, and music.",
            "https://images.unsplash.com/photo-1574096079513-d8259312b785?auto=format&fit=crop&w=800&q=80"
        )
    )

    override fun getPlaces(): Flow<List<Place>> = flowOf(mockPlaces)

    override fun getTrendingPlaces(): Flow<List<Place>> = flowOf(mockPlaces.sortedByDescending { it.saves }.take(3))

    override fun getPlaceById(id: String): Flow<Place?> = flowOf(mockPlaces.find { it.id == id })

    override suspend fun searchPlaces(query: String): List<Place> {
        return mockPlaces.filter { 
            it.name.contains(query, ignoreCase = true) || 
            it.location.contains(query, ignoreCase = true) ||
            it.category.contains(query, ignoreCase = true) ||
            it.vibe.any { vibe -> vibe.contains(query, ignoreCase = true) }
        }
    }

    override fun getPlacesByUser(userId: String): Flow<List<Place>> = flowOf(emptyList())

    override fun getSavedPlaces(userId: String): Flow<List<Place>> = flowOf(emptyList())

    override fun isPlaceSaved(userId: String, placeId: String): Flow<Boolean> = flowOf(false)

    override suspend fun toggleSavePlace(
        userId: String,
        placeId: String,
        isSaved: Boolean
    ): Result<Unit> = Result.success(Unit)

    override suspend fun addPlace(place: Place) {
        // No-op for mock
    }
}
