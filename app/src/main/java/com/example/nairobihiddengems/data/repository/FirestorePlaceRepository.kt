package com.example.nairobihiddengems.data.repository

import android.util.Log
import com.example.nairobihiddengems.domain.models.Place
import com.example.nairobihiddengems.domain.repository.PlaceRepository
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirestorePlaceRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage
) : PlaceRepository {

    private val TAG = "FirestoreRepo"
    private val placesCollection = firestore.collection("places")
    private val storageRef = storage.reference.child("place_images")

    override fun getPlaces(): Flow<List<Place>> = callbackFlow {
        val subscription = placesCollection
            .whereEqualTo("status", "approved")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e(TAG, "Error fetching places: ${error.message}", error)
                    close(error)
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val places = snapshot.documents.mapNotNull { doc ->
                        doc.toPlace()
                    }
                    Log.d(TAG, "Fetched ${places.size} approved places")
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

    override fun getPlacesByUser(userId: String): Flow<List<Place>> = callbackFlow {
        val subscription = placesCollection.whereEqualTo("createdBy", userId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val places = snapshot.documents.mapNotNull { it.toPlace() }
                    trySend(places)
                }
            }
        awaitClose { subscription.remove() }
    }

    override fun getSavedPlaces(userId: String): Flow<List<Place>> = callbackFlow {
        val subscription = firestore.collection("users").document(userId)
            .collection("savedGems")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                
                launch {
                    val placeIds = snapshot?.documents?.map { it.id } ?: emptyList()
                    if (placeIds.isEmpty()) {
                        trySend(emptyList())
                    } else {
                        try {
                            // Batch fetch places (limit 10 per whereIn query)
                            val places = placeIds.chunked(10).flatMap { ids ->
                                placesCollection.whereIn(FieldPath.documentId(), ids)
                                    .get().await().documents.mapNotNull { it.toPlace() }
                            }
                            trySend(places)
                        } catch (e: Exception) {
                            Log.e(TAG, "Error fetching saved places details", e)
                            trySend(emptyList())
                        }
                    }
                }
            }
        awaitClose { subscription.remove() }
    }

    override fun isPlaceSaved(userId: String, placeId: String): Flow<Boolean> = callbackFlow {
        val subscription = firestore.collection("users").document(userId)
            .collection("savedGems").document(placeId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                trySend(snapshot?.exists() == true)
            }
        awaitClose { subscription.remove() }
    }

    override suspend fun toggleSavePlace(
        userId: String,
        placeId: String,
        isSaved: Boolean
    ): Result<Unit> {
        return try {
            val userSavedRef = firestore.collection("users").document(userId)
                .collection("savedGems").document(placeId)
            val placeRef = placesCollection.document(placeId)

            firestore.runTransaction { transaction ->
                val placeSnapshot = transaction.get(placeRef)
                val currentSaves = placeSnapshot.getLong("saves") ?: 0L
                
                if (isSaved) {
                    transaction.set(userSavedRef, mapOf("savedAt" to com.google.firebase.Timestamp.now()))
                    transaction.update(placeRef, "saves", currentSaves + 1)
                } else {
                    transaction.delete(userSavedRef)
                    transaction.update(placeRef, "saves", (currentSaves - 1).coerceAtLeast(0))
                }
            }.await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
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
                imageUrl = getString("imageUrl") ?: "",
                createdBy = getString("createdBy"),
                status = getString("status") ?: "approved"
            )
        } catch (e: Exception) {
            null
        }
    }
    
    // For seeding data as requested
    override suspend fun addPlace(place: Place) {
        val data = hashMapOf(
            "name" to place.name,
            "location" to place.location,
            "category" to place.category,
            "vibe" to place.vibe,
            "saves" to place.saves,
            "rating" to place.rating,
            "description" to place.description,
            "imageUrl" to place.imageUrl,
            "createdBy" to place.createdBy,
            "status" to place.status
        )
        try {
            placesCollection.add(data).await()
            Log.d(TAG, "Successfully added place: ${place.name}")
        } catch (e: Exception) {
            Log.e(TAG, "Error adding place: ${e.message}", e)
            throw e
        }
    }

    override suspend fun uploadPlaceImage(uri: android.net.Uri): Result<String> {
        return try {
            val fileName = "${System.currentTimeMillis()}_${uri.lastPathSegment}"
            val imageRef = storageRef.child(fileName)
            val uploadTask = imageRef.putFile(uri).await()
            val downloadUrl = imageRef.downloadUrl.await().toString()
            Result.success(downloadUrl)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
