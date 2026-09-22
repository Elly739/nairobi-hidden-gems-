package com.example.nairobihiddengems.domain.models

data class Place(
    val id: String,
    val name: String,
    val location: String,
    val category: String,
    val vibe: List<String>,
    val saves: Int,
    val rating: Double,
    val description: String,
    val imageUrl: String,
    val createdBy: String? = null,
    val status: String = "approved" // Default to approved for initial seed/dev, change to pending for production
)
