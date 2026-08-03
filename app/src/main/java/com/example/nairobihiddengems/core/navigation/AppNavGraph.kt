package com.example.nairobihiddengems.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.nairobihiddengems.ui.add.AddGemScreen
import com.example.nairobihiddengems.ui.details.PlaceDetailScreen
import com.example.nairobihiddengems.ui.explore.ExploreScreen
import com.example.nairobihiddengems.ui.home.HomeScreen
import com.example.nairobihiddengems.ui.profile.ProfileScreen
import com.example.nairobihiddengems.ui.saved.SavedScreen
import com.example.nairobihiddengems.ui.splash.SplashScreen

@Composable
fun AppNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    onSplashFinished: () -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route,
        modifier = modifier
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(onSplashFinished = onSplashFinished)
        }
        composable(Screen.Home.route) {
            HomeScreen(
                onPlaceClick = { placeId ->
                    navController.navigate("details/$placeId")
                }
            )
        }
        composable(Screen.Explore.route) {
            ExploreScreen()
        }
        composable(Screen.Add.route) {
            AddGemScreen()
        }
        composable(Screen.Saved.route) {
            SavedScreen()
        }
        composable(Screen.Profile.route) {
            ProfileScreen()
        }
        composable(
            route = Screen.Details.route,
            arguments = listOf(navArgument("placeId") { type = NavType.StringType })
        ) { backStackEntry ->
            val placeId = backStackEntry.arguments?.getString("placeId")
            PlaceDetailScreen(
                placeId = placeId,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
