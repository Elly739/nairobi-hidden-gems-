package com.example.nairobihiddengems.domain.models

data class Place(
    val id: String,
    val name: String,
    val location: String,
    val category: String,
    val rating: Double,
    val description: String,
    val imageUrl: String
)
