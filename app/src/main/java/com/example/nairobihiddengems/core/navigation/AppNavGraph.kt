package com.example.nairobihiddengems.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.nairobihiddengems.ui.auth.AuthViewModel
import com.example.nairobihiddengems.ui.auth.LoginScreen
import com.example.nairobihiddengems.ui.auth.RegisterScreen
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
    authViewModel: AuthViewModel = hiltViewModel(),
    onSplashFinished: () -> Unit
) {
    val currentUser by authViewModel.currentUser.collectAsStateWithLifecycle()

    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route,
        modifier = modifier
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(onSplashFinished = {
                if (currentUser != null) {
                    onSplashFinished()
                } else {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            })
        }
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(Screen.Register.route)
                }
            )
        }
        composable(Screen.Register.route) {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Register.route) { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.popBackStack()
                }
            )
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
            ProfileScreen(
                onLogout = {
                    authViewModel.signOut()
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                }
            )
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
