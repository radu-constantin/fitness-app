package com.example.strengthlog

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
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
        composable("profile") { ProfileScreen() }
        composable("active_workout") { ActiveWorkoutScreen(navController) }
        composable("exercise_browser") { ExerciseBrowserScreen() }
        composable("workout_details") { WorkoutDetailsScreen() }
        composable("login") { LoginScreen(navController) }
        composable("register") { RegisterScreen(navController) }
    }
}