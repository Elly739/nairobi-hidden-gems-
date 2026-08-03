package com.example.nairobihiddengems.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.nairobihiddengems.domain.models.Place
import com.example.nairobihiddengems.ui.home.components.PlaceCard

@Composable
fun HomeScreen() {
    val mockPlaces = listOf(
        Place(
            "1", "The Alchemist", "Westlands", "Bar & Creative Hub", 4.5,
            "An open-air creative space featuring bars, food trucks, and music.",
            "https://images.unsplash.com/photo-1514933651103-005eec06c04b?auto=format&fit=crop&w=800&q=80"
        ),
        Place(
            "2", "Karura Forest", "Limuru Road", "Nature", 4.8,
            "One of the largest gazetted forests in the world fully within city limits.",
            "https://images.unsplash.com/photo-1441974231531-c6227db76b6e?auto=format&fit=crop&w=800&q=80"
        ),
        Place(
            "3", "Karen Blixen Museum", "Karen", "History", 4.6,
            "The historic home of the author of 'Out of Africa'.",
            "https://images.unsplash.com/photo-1582719508461-905c673771fd?auto=format&fit=crop&w=800&q=80"
        ),
        Place(
            "4", "The Secret Garden", "Riverside", "Cafe", 4.7,
            "Aesthetic brunch spot hidden away in the heart of the city.",
            "https://images.unsplash.com/photo-1554118811-1e0d58224f24?auto=format&fit=crop&w=800&q=80"
        )
    )

    Scaffold(
        topBar = {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Discover",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Nairobi's Hidden Gems",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            items(mockPlaces) { place ->
                PlaceCard(place = place)
            }
        }
    }
}
