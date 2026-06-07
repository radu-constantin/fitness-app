package com.example.strengthlog.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.strengthlog.viewmodels.HomeViewModel
import com.example.strengthlog.viewmodels.WorkoutSummary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    val viewModel: HomeViewModel = viewModel()
    val workouts by viewModel.workouts.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("StrengthLog") },
                actions = {
                    IconButton(onClick = { navController.navigate("profile") }) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Profile"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate("active_workout") }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Start workout",
                    modifier = Modifier.size(30.dp)
                )
            }
        }
    ) { innerPadding ->
        if (workouts.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("No workouts yet", style = MaterialTheme.typography.titleMedium)
                    Text("Tap + to log your first workout", style = MaterialTheme.typography.bodyMedium)
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(workouts) { summary ->
                    WorkoutCard(
                        summary = summary,
                        onClick = { navController.navigate("workout_details/${summary.workout.id}") },
                        onDelete = { viewModel.deleteWorkout(summary.workout) }
                    )
                }
            }
        }
    }
}

@Composable
fun WorkoutCard(
    summary: WorkoutSummary,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = summary.workout.name,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = onDelete) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete workout"
                    )
                }
            }
            Text(
                text = formatDate(summary.workout.timeStart),
                style = MaterialTheme.typography.bodyMedium
            )
            if (summary.previewExercises.isNotEmpty()) {
                Text(
                    text = summary.previewExercises.joinToString(" · "),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Text(
                text = formatDuration(summary.workout.timeStart, summary.workout.timeEnd),
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

private fun formatDuration(timeStart: Long, timeEnd: Long?): String {
    if (timeEnd == null) return "In progress"
    val minutes = ((timeEnd - timeStart) / 1000 / 60).toInt()
    return if (minutes < 60) "${minutes}m" else "${minutes / 60}h ${minutes % 60}m"
}

private fun formatDate(timeStart: Long): String {
    val sdf = java.text.SimpleDateFormat("dd MMM yyyy", java.util.Locale.getDefault())
    return sdf.format(java.util.Date(timeStart))
}