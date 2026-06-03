package com.example.strengthlog.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.example.strengthlog.viewmodels.ExerciseWithSets
import com.example.strengthlog.viewmodels.WorkoutDetailsViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutDetailsScreen(navController: NavController) {
    val viewModel: WorkoutDetailsViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(uiState.workout?.name ?: "Workout") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                uiState.workout?.let { workout ->
                    item {
                        val dateStr = SimpleDateFormat("MMM dd, yyyy  HH:mm", Locale.getDefault())
                            .format(Date(workout.timeStart))
                        Text(
                            text = dateStr,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        if (workout.timeEnd != null) {
                            val durationMin = ((workout.timeEnd - workout.timeStart) / 60000).toInt()
                            Text(
                                text = "Duration: $durationMin min",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                        if (!workout.note.isNullOrEmpty()) {
                            Text(
                                text = workout.note,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }

                items(uiState.exercisesWithSets) { exerciseWithSets ->
                    ExerciseSetCard(
                        exerciseWithSets = exerciseWithSets,
                        weightUnit = uiState.weightUnit
                    )
                }
            }
        }
    }
}

@Composable
fun ExerciseSetCard(exerciseWithSets: ExerciseWithSets, weightUnit: String) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = exerciseWithSets.exercise.name,
                style = MaterialTheme.typography.titleMedium
            )
            exerciseWithSets.sets.forEach { set ->
                Text(
                    text = "Set ${set.setNumber}:  ${set.reps} reps × ${set.weight} $weightUnit",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}