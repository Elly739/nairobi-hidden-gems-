package com.example.nairobihiddengems.ui.explore

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun ExploreScreen(
    onPlaceClick: (String) -> Unit = {},
    viewModel: ExploreViewModel = hiltViewModel()
) {
    val places by viewModel.places.collectAsStateWithLifecycle()
    val nairobiCenter = LatLng(-1.2921, 36.8219)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(nairobiCenter, 12f)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState
        ) {
            places.forEach { place ->
                val lat = place.latitude ?: -1.2921
                val lng = place.longitude ?: 36.8219
                val position = LatLng(lat, lng)
                Marker(
                    state = MarkerState(position = position),
                    title = place.name,
                    snippet = place.location,
                    onClick = {
                        onPlaceClick(place.id)
                        false
                    }
                )
            }
        }
    }
}
