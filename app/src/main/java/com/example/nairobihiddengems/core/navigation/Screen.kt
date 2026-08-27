package com.example.nairobihiddengems.core.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(
    val route: String,
    val title: String? = null,
    val icon: ImageVector? = null
) {
    object Splash : Screen("splash")
    object Login : Screen("login")
    object Register : Screen("register")
    object Home : Screen("home", "Home", Icons.Default.Home)
    object Explore : Screen("explore", "Explore", Icons.Default.Search)
    object Add : Screen("add", "Add", Icons.Default.Add)
    object Saved : Screen("saved", "Saved", Icons.Default.Folder)
    object Profile : Screen("profile", "Profile", Icons.Default.Person)
    object Details : Screen("details/{placeId}")

    companion object {
        val bottomNavItems = listOf(Home, Saved, Profile)
    }
}
