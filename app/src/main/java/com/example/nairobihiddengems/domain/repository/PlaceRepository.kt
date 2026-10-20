package com.example.nairobihiddengems.domain.repository

import com.example.nairobihiddengems.domain.models.Place
import kotlinx.coroutines.flow.Flow

interface PlaceRepository {
    fun getPlaces(): Flow<List<Place>>
    fun getTrendingPlaces(): Flow<List<Place>>
    fun getPlaceById(id: String): Flow<Place?>
    suspend fun searchPlaces(query: String): List<Place>
    fun getPlacesByUser(userId: String): Flow<List<Place>>
    
    // Saved Places
    fun getSavedPlaces(userId: String): Flow<List<Place>>
    fun isPlaceSaved(userId: String, placeId: String): Flow<Boolean>
    suspend fun toggleSavePlace(userId: String, placeId: String, isSaved: Boolean): Result<Unit>
    
    // Reposts & Feed
    suspend fun toggleRepost(userId: String, placeId: String, isReposted: Boolean): Result<Unit>
    fun getFollowingFeed(followingIds: List<String>): Flow<List<Place>>
    fun getRepostedPlaces(userId: String): Flow<List<Place>>

    suspend fun addPlace(place: Place)
    suspend fun uploadPlaceImage(uri: android.net.Uri): Result<String>
}
