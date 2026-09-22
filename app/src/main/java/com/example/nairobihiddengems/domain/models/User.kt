package com.example.nairobihiddengems.domain.models

data class User(
    val uid: String = "",
    val email: String = "",
    val displayName: String? = null,
    val bio: String? = null,
    val profilePicUrl: String? = null,
    val coverImageUrl: String? = null,
    val followerCount: Int = 0,
    val followingCount: Int = 0,
    val dropsCount: Int = 0,
    val savedCount: Int = 0,
    val isPioneer: Boolean = false
)
