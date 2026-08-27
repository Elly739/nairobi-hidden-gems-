package com.example.nairobihiddengems.data.repository

import android.util.Log
import com.example.nairobihiddengems.domain.models.Place
import com.example.nairobihiddengems.domain.repository.PlaceRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirestorePlaceRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) : PlaceRepository {

    private val TAG = "FirestoreRepo"
    private val placesCollection = firestore.collection("places")

    override fun getPlaces(): Flow<List<Place>> = callbackFlow {
        val subscription = placesCollection.addSnapshotListener { snapshot, error ->
            if (error != null) {
                Log.e(TAG, "Error fetching places: ${error.message}", error)
                close(error)
                return@addSnapshotListener
            }
            if (snapshot != null) {
                val places = snapshot.documents.mapNotNull { doc ->
                    doc.toPlace()
                }
                Log.d(TAG, "Fetched ${places.size} places")
                trySend(places)
            }
        }
        awaitClose { subscription.remove() }
    }

    override fun getTrendingPlaces(): Flow<List<Place>> = callbackFlow {
        val subscription = placesCollection
            .orderBy("saves", Query.Direction.DESCENDING)
            .limit(5)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val places = snapshot.documents.mapNotNull { doc ->
                        doc.toPlace()
                    }
                    trySend(places)
                }
            }
        awaitClose { subscription.remove() }
    }

    override fun getPlaceById(id: String): Flow<Place?> = callbackFlow {
        val subscription = placesCollection.document(id).addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }
            trySend(snapshot?.toPlace())
        }
        awaitClose { subscription.remove() }
    }

    override suspend fun searchPlaces(query: String): List<Place> {
        // Firestore doesn't support full-text search easily without Algolia/ElasticSearch.
        // For a small app, we can fetch all and filter, or use a simple prefix query.
        val snapshot = placesCollection.get().await()
        return snapshot.documents.mapNotNull { it.toPlace() }.filter { 
            it.name.contains(query, ignoreCase = true) || 
            it.location.contains(query, ignoreCase = true)
        }
    }

    // Helper extension to map Firestore document to Place model
    private fun com.google.firebase.firestore.DocumentSnapshot.toPlace(): Place? {
        return try {
            Place(
                id = id,
                name = getString("name") ?: "",
                location = getString("location") ?: "",
                category = getString("category") ?: "",
                vibe = (get("vibe") as? List<*>)?.map { it.toString() } ?: emptyList(),
                saves = getLong("saves")?.toInt() ?: 0,
                rating = getDouble("rating") ?: 0.0,
                description = getString("description") ?: "",
                imageUrl = getString("imageUrl") ?: ""
            )
        } catch (e: Exception) {
            null
        }
    }
    
    // For seeding data as requested
    suspend fun addPlace(place: Place) {
        val data = hashMapOf(
            "name" to place.name,
            "location" to place.location,
            "category" to place.category,
            "vibe" to place.vibe,
            "saves" to place.saves,
            "rating" to place.rating,
            "description" to place.description,
            "imageUrl" to place.imageUrl
        )
        try {
            placesCollection.add(data).await()
            Log.d(TAG, "Successfully added place: ${place.name}")
        } catch (e: Exception) {
            Log.e(TAG, "Error adding place: ${e.message}", e)
            throw e
        }
    }
}
