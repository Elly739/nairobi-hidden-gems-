package com.example.nairobihiddengems.ui.add

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nairobihiddengems.core.theme.PrimaryPurple
import com.example.nairobihiddengems.core.theme.SecondaryPink

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddGemScreen(
    viewModel: AddGemViewModel = hiltViewModel()
) {
    var placeName by remember { mutableStateOf("") }
    var area by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Café") }
    var description by remember { mutableStateOf("") }
    var isSearchSelected by remember { mutableStateOf(true) }
    
    var unsplashQuery by remember { mutableStateOf("") }
    
    var aestheticRating by remember { mutableStateOf(4.0) }
    var chillRating by remember { mutableStateOf(4.0) }
    var crowdRating by remember { mutableStateOf(4.0) }

    val selectedImageUri by viewModel.selectedImageUri.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        viewModel.onImageSelected(uri)
    }

    LaunchedEffect(uiState) {
        if (uiState is AddGemUiState.Success) {
            placeName = ""
            area = ""
            description = ""
            viewModel.resetState()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Drop a Hidden Gem ✨",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Share Nairobi's best spots with the community",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(text = "Choose Image", fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Row(modifier = Modifier.fillMaxWidth()) {
            Button(
                onClick = { isSearchSelected = true },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isSearchSelected) PrimaryPurple else MaterialTheme.colorScheme.surfaceVariant
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.Search, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Search")
            }
            Spacer(modifier = Modifier.width(16.dp))
            Button(
                onClick = { 
                    isSearchSelected = false
                    imagePickerLauncher.launch("image/*")
                },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (!isSearchSelected) PrimaryPurple else MaterialTheme.colorScheme.surfaceVariant
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.Upload, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Upload")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (isSearchSelected) {
            OutlinedTextField(
                value = unsplashQuery,
                onValueChange = { 
                    unsplashQuery = it
                    if (it.length > 3) {
                        // Simulate Unsplash selection
                        viewModel.onImageSelected(android.net.Uri.parse("https://source.unsplash.com/featured/?nairobi,${it}"))
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Search Unsplash (e.g. 'cafe', 'sunset')...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                shape = RoundedCornerShape(12.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                    disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                    unfocusedIndicatorColor = Color.Transparent
                )
            )
        }

        if (selectedImageUri != null) {
            Spacer(modifier = Modifier.height(16.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.DarkGray)
            ) {
                coil.compose.AsyncImage(
                    model = selectedImageUri,
                    contentDescription = "Selected",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = androidx.compose.ui.layout.ContentScale.Crop
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        InputFieldLabel("Place Name *")
        OutlinedTextField(
            value = placeName,
            onValueChange = { placeName = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("e.g. Java House Westlands Roof") },
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row {
            Column(modifier = Modifier.weight(1f)) {
                InputFieldLabel("Area *")
                OutlinedTextField(
                    value = area,
                    onValueChange = { area = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Select area") },
                    shape = RoundedCornerShape(12.dp)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                InputFieldLabel("Category *")
                OutlinedTextField(
                    value = category,
                    onValueChange = { category = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("☕ Café") },
                    shape = RoundedCornerShape(12.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        InputFieldLabel("Why should we go? *")
        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            modifier = Modifier.fillMaxWidth().height(100.dp),
            placeholder = { Text("Best lighting at 5pm, quiet corners...") },
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            RatingInput("Aesthetic", aestheticRating.toString()) { aestheticRating = it.toDouble() }
            RatingInput("Chill", chillRating.toString()) { chillRating = it.toDouble() }
            RatingInput("Crowd", crowdRating.toString()) { crowdRating = it.toDouble() }
        }

        Spacer(modifier = Modifier.height(32.dp))

        if (uiState is AddGemUiState.Error) {
            Text(
                text = (uiState as AddGemUiState.Error).message,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        Button(
            onClick = { 
                viewModel.postGem(
                    placeName, area, category, description, 
                    aestheticRating, chillRating, crowdRating
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .background(
                    Brush.horizontalGradient(listOf(PrimaryPurple, SecondaryPink)),
                    RoundedCornerShape(16.dp)
                ),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
            enabled = uiState !is AddGemUiState.Loading && placeName.isNotEmpty() && area.isNotEmpty()
        ) {
            if (uiState is AddGemUiState.Loading) {
                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
            } else {
                Text("🚀 Post Gem", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            }
        }
        
        Spacer(modifier = Modifier.height(100.dp))
    }
}

@Composable
fun InputFieldLabel(text: String) {
    Text(
        text = text,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        modifier = Modifier.padding(bottom = 8.dp)
    )
}

@Composable
fun RatingInput(label: String, value: String, onValueChange: (Float) -> Unit) {
    var rating by remember { mutableStateOf(value.toFloat()) }
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = label, fontSize = 12.sp, color = Color.Gray)
        Spacer(modifier = Modifier.height(4.dp))
        Row {
            repeat(1) { // Simple one for now, or just keep the box but make it clickable
                Box(
                    modifier = Modifier
                        .size(width = 80.dp, height = 40.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .clickable { 
                            rating = if (rating >= 5f) 1f else rating + 0.5f
                            onValueChange(rating)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = rating.toString(), fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
