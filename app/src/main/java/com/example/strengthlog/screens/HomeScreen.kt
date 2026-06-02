package com.example.strengthlog.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.strengthlog.data.local.entity.WorkoutEntity
import com.example.strengthlog.viewmodels.HomeViewModel
import com.google.firebase.auth.FirebaseAuth

data class Workout(val name: String, val date: String)

val mockWorkouts = listOf(
    Workout("Push Day", "May 28, 2026"),
    Workout("Leg Day", "May 26, 2026"),
    Workout("Pull Day", "May 24, 2026")
)

@Composable
fun HomeScreen(navController: NavController) {
    val viewModel: HomeViewModel = viewModel()
    val workouts by viewModel.workouts.collectAsState()
    val currentUser = FirebaseAuth.getInstance().currentUser

    LaunchedEffect(currentUser?.uid) {
        currentUser?.uid?.let { viewModel.loadWorkouts(it) }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate("active_workout") }) {
                Text(text = "+")
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(workouts) { workout ->
                WorkoutCard(workout = workout)
            }
        }
    }
}

@Composable
fun WorkoutCard(workout: WorkoutEntity) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = workout.name, style = MaterialTheme.typography.titleMedium)
        }
    }
}