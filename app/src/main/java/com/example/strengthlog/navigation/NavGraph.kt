package com.example.strengthlog.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.strengthlog.screens.ActiveWorkoutScreen
import com.example.strengthlog.screens.ExerciseBrowserScreen
import com.example.strengthlog.screens.HomeScreen
import com.example.strengthlog.screens.LoginScreen
import com.example.strengthlog.screens.ProfileScreen
import com.example.strengthlog.screens.RegisterScreen
import com.example.strengthlog.screens.WorkoutDetailsScreen
import com.google.firebase.auth.FirebaseAuth

@Composable
fun NavGraph() {
    val navController = rememberNavController()
    val startDestination = if (FirebaseAuth.getInstance().currentUser != null) "home" else "login"

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable("home") { HomeScreen(navController) }
        composable("profile") { ProfileScreen(navController) }
        composable(
            route = "active_workout?workoutId={workoutId}",
            arguments = listOf(navArgument("workoutId") {
                type = NavType.IntType
                defaultValue = -1
            })
        ) { ActiveWorkoutScreen(navController) }
        composable("exercise_browser") { ExerciseBrowserScreen(navController) }
        composable(
            route = "workout_details/{workoutId}",
            arguments = listOf(navArgument("workoutId") { type = NavType.IntType })
        ) {
            WorkoutDetailsScreen(navController)
        }
        composable("login") { LoginScreen(navController) }
        composable("register") { RegisterScreen(navController) }
    }
}