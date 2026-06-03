package com.example.strengthlog.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.strengthlog.data.local.entity.WorkoutExerciseEntity
import com.example.strengthlog.viewmodels.ActiveWorkoutViewModel
import com.example.strengthlog.viewmodels.ExerciseWithSets

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActiveWorkoutScreen(navController: NavController) {
    val viewModel: ActiveWorkoutViewModel = viewModel()

    val workoutName by viewModel.workoutName.collectAsState()
    val exercisesWithSets by viewModel.exercisesWithSets.collectAsState()
    val elapsedSeconds by viewModel.elapsedSeconds.collectAsState()
    val isFinished by viewModel.isFinished.collectAsState()

    LaunchedEffect(isFinished) {
        if (isFinished) navController.popBackStack()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    OutlinedTextField(
                        value = workoutName,
                        onValueChange = { viewModel.updateWorkoutName(it) },
                        singleLine = true,
                        textStyle = MaterialTheme.typography.titleMedium
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    Text(
                        text = formatElapsedTime(elapsedSeconds),
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Button(
                        onClick = { viewModel.finishWorkout() },
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Text("Finish")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("exercise_browser") }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Exercise")
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            items(exercisesWithSets, key = { it.exercise.exerciseId }) { exerciseWithSets ->
                ExerciseCard(
                    exerciseWithSets = exerciseWithSets,
                    onAddSet = { viewModel.addSet(exerciseWithSets.exercise) },
                    onSetUpdated = { viewModel.updateSet(it) }
                )
            }
        }
    }
}

@Composable
fun ExerciseCard(
    exerciseWithSets: ExerciseWithSets,
    onAddSet: () -> Unit,
    onSetUpdated: (WorkoutExerciseEntity) -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = exerciseWithSets.exercise.name,
                style = MaterialTheme.typography.titleMedium
            )

            if (exerciseWithSets.sets.isNotEmpty()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("Set", modifier = Modifier.width(32.dp),
                        style = MaterialTheme.typography.labelMedium)
                    Text("Reps", modifier = Modifier.weight(1f),
                        style = MaterialTheme.typography.labelMedium)
                    Text("kg", modifier = Modifier.weight(1f),
                        style = MaterialTheme.typography.labelMedium)
                    Spacer(modifier = Modifier.size(48.dp))
                }

                exerciseWithSets.sets.forEach { set ->
                    SetRow(set = set, onSetUpdated = onSetUpdated)
                }
            }

            TextButton(onClick = onAddSet) {
                Icon(Icons.Default.Add, contentDescription = null)
                Spacer(Modifier.width(4.dp))
                Text("Add Set")
            }
        }
    }
}

@Composable
fun SetRow(
    set: WorkoutExerciseEntity,
    onSetUpdated: (WorkoutExerciseEntity) -> Unit
) {
    var repsText by remember(set.id) {
        mutableStateOf(if (set.reps == 0) "" else set.reps.toString())
    }
    var weightText by remember(set.id) {
        mutableStateOf(if (set.weight == 0f) "" else set.weight.toString())
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = set.setNumber.toString(),
            modifier = Modifier.width(32.dp),
            style = MaterialTheme.typography.bodyMedium
        )
        OutlinedTextField(
            value = repsText,
            onValueChange = { repsText = it },
            label = { Text("Reps") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.weight(1f),
            singleLine = true
        )
        OutlinedTextField(
            value = weightText,
            onValueChange = { weightText = it },
            label = { Text("kg") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            modifier = Modifier.weight(1f),
            singleLine = true
        )
        // Saves reps + weight to Room for this set
        IconButton(
            onClick = {
                val reps = repsText.toIntOrNull() ?: 0
                val weight = weightText.toFloatOrNull() ?: 0f
                onSetUpdated(set.copy(reps = reps, weight = weight))
            }
        ) {
            Icon(Icons.Default.Check, contentDescription = "Save set")
        }
    }
}

private fun formatElapsedTime(seconds: Long): String {
    val minutes = seconds / 60
    val secs = seconds % 60
    return "%02d:%02d".format(minutes, secs)
}