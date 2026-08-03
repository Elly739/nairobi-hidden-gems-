package com.example.nairobihiddengems.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nairobihiddengems.core.theme.PrimaryPurple
import com.example.nairobihiddengems.core.theme.SecondaryPink
import com.example.nairobihiddengems.domain.models.Place
import com.example.nairobihiddengems.ui.components.VibeChip
import com.example.nairobihiddengems.ui.home.components.PlaceCard
import com.example.nairobihiddengems.ui.home.components.TrendingCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(onPlaceClick: (String) -> Unit) {
    val mockPlaces = remember {
        listOf(
            Place(
                "1", "Kiza Rooftop Lounge", "Westlands", "Night", 4.9,
                "The ultimate rooftop experience in Nairobi.",
                "https://images.unsplash.com/photo-1514933651103-005eec06c04b?auto=format&fit=crop&w=800&q=80"
            ),
            Place(
                "2", "The Java House Garden", "Kilimani", "Cafe", 4.6,
                "A serene spot for your morning coffee.",
                "https://images.unsplash.com/photo-1554118811-1e0d58224f24?auto=format&fit=crop&w=800&q=80"
            ),
            Place(
                "3", "Ngong Road Sunset Point", "Ngong Road", "Sunset", 4.8,
                "Best sunset view in the city.",
                "https://images.unsplash.com/photo-1441974231531-c6227db76b6e?auto=format&fit=crop&w=800&q=80"
            ),
            Place(
                "4", "Artcaffe Market", "Westlands", "Cafe", 4.5,
                "Study and grind at this aesthetic market cafe.",
                "https://images.unsplash.com/photo-1582719508461-905c673771fd?auto=format&fit=crop&w=800&q=80"
            )
        )
    }

    val vibes = listOf("All Vibes", "☕ Cafe", "🌅 Sunset", "📚 Study", "🌙 Night")
    var selectedVibe by remember { mutableStateOf("All Vibes") }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 100.dp) // Space for bottom bar
    ) {
        // Custom Top Bar
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Notifications, // Placeholder for Pin icon
                        contentDescription = null,
                        tint = SecondaryPink
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Nairobi ",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Hidden Gems",
                        style = MaterialTheme.typography.titleLarge,
                        color = PrimaryPurple,
                        fontWeight = FontWeight.Bold
                    )
                }
                IconButton(onClick = { }) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "Notifications",
                        tint = Color.Yellow // As per vision
                    )
                }
            }
        }

        // Trending Section
        item {
            Column(modifier = Modifier.padding(start = 16.dp, bottom = 24.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(end = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Trending Now 🔥",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "↺ Refresh",
                        style = MaterialTheme.typography.labelMedium,
                        color = PrimaryPurple
                    )
                }
                Text(
                    text = "Most saved this week in Nairobi",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
                Spacer(modifier = Modifier.height(16.dp))
                LazyRow {
                    items(mockPlaces.reversed()) { place ->
                        TrendingCard(
                            place = place,
                            onClick = { onPlaceClick(place.id) }
                        )
                    }
                }
            }
        }

        // Search Bar
        item {
            OutlinedTextField(
                value = "",
                onValueChange = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(12.dp)),
                placeholder = { Text("Search gems...", color = Color.Gray) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = PrimaryPurple) },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    focusedIndicatorColor = PrimaryPurple,
                    unfocusedIndicatorColor = Color.Transparent,
                ),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(24.dp))
        }

        // Vibe Filters
        item {
            Column(modifier = Modifier.padding(start = 16.dp, bottom = 16.dp)) {
                Text(
                    text = "Filter by Vibe",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(12.dp))
                LazyRow {
                    items(vibes) { vibe ->
                        VibeChip(
                            text = vibe.split(" ").last(),
                            icon = if (vibe.contains(" ")) vibe.split(" ").first() else null,
                            isSelected = selectedVibe == vibe,
                            onClick = { selectedVibe = vibe }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                }
            }
        }

        // Main Grid (Simulated using item and chunks because LazyColumn cannot nest LazyVerticalGrid directly easily without fixed height)
        // I will use a simple column of rows to simulate the grid for simplicity in this skeleton
        items(mockPlaces.chunked(2)) { rowPlaces ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
            ) {
                rowPlaces.forEach { place ->
                    PlaceCard(
                        place = place,
                        onClick = { onPlaceClick(place.id) },
                        modifier = Modifier.weight(1f)
                    )
                }
                if (rowPlaces.size == 1) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}
