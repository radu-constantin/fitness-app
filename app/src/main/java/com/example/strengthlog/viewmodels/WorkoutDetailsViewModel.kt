package com.example.strengthlog.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.strengthlog.StrengthLogApplication
import com.example.strengthlog.data.local.entity.WorkoutEntity
import com.example.strengthlog.data.preferences.PreferencesManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class WorkoutDetailsUiState(
    val workout: WorkoutEntity? = null,
    val exercisesWithSets: List<ExerciseWithSets> = emptyList(),
    val weightUnit: String = PreferencesManager.UNIT_KG,
    val isLoading: Boolean = true
)

class WorkoutDetailsViewModel(
    application: Application,
    savedStateHandle: SavedStateHandle
) : AndroidViewModel(application) {

    private val workoutRepository = (application as StrengthLogApplication).workoutRepository
    private val exerciseRepository = (application as StrengthLogApplication).exerciseRepository
    private val preferencesManager = (application as StrengthLogApplication).preferencesManager

    private val workoutId: Int = checkNotNull(savedStateHandle["workoutId"])

    private val _uiState = MutableStateFlow(WorkoutDetailsUiState())
    val uiState: StateFlow<WorkoutDetailsUiState> = _uiState

    init {
        loadWorkoutDetails()
    }

    private fun loadWorkoutDetails() {
        viewModelScope.launch {
            val workout = workoutRepository.getWorkoutById(workoutId)
            workoutRepository.getExercisesForWorkout(workoutId).collect { entries ->
                val exercisesWithSets = entries
                    .groupBy { it.exerciseId }
                    .mapNotNull { (exerciseId, sets) ->
                        val exercise = exerciseRepository.getExerciseById(exerciseId)
                            ?: return@mapNotNull null
                        ExerciseWithSets(
                            exercise = exercise,
                            sets = sets.sortedBy { it.setNumber }
                        )
                    }
                _uiState.value = WorkoutDetailsUiState(
                    workout = workout,
                    exercisesWithSets = exercisesWithSets,
                    weightUnit = preferencesManager.weightUnit,
                    isLoading = false
                )
            }
        }
    }
}