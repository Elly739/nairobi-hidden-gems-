package com.example.nairobihiddengems.domain.repository

import com.example.nairobihiddengems.domain.models.Place
import kotlinx.coroutines.flow.Flow

interface PlaceRepository {
    fun getPlaces(): Flow<List<Place>>
    fun getTrendingPlaces(): Flow<List<Place>>
    fun getPlaceById(id: String): Flow<Place?>
    suspend fun searchPlaces(query: String): List<Place>
}
