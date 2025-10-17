package com.example.laboratorio8.screens

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.laboratorio8.data.PhotoRepository

@Composable
fun AppNavigation(
    repository: PhotoRepository,
    isNetworkAvailable: () -> Boolean,
    isDarkMode: Boolean,
    toggleTheme: () -> Unit
) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(
                repository = repository,
                isNetworkAvailable = isNetworkAvailable,
                onPhotoClick = { photoId ->
                    navController.navigate("details/$photoId")
                },
                isDarkMode = isDarkMode,
                toggleTheme = toggleTheme
            )
        }
        composable(
            route = "details/{photoId}",
            arguments = listOf(navArgument("photoId") { type = NavType.StringType })
        ) { backStackEntry ->
            val photoId = backStackEntry.arguments?.getString("photoId") ?: ""
            DetailsScreen(
                repository = repository,
                photoId = photoId,
                onNavigateBack = { navController.popBackStack() },
                isNetworkAvailable = isNetworkAvailable
            )
        }
    }
}
