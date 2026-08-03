package com.example.nairobihiddengems.core.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(
    val route: String,
    val title: String? = null,
    val icon: ImageVector? = null
) {
    object Splash : Screen("splash")
    object Home : Screen("home", "Home", Icons.Default.Home)
    object Explore : Screen("explore", "Explore", Icons.Default.Search)
    object Saved : Screen("saved", "Saved", Icons.Default.Favorite)
    object Profile : Screen("profile", "Profile", Icons.Default.AccountCircle)

    companion object {
        val bottomNavItems = listOf(Home, Explore, Saved, Profile)
    }
}
