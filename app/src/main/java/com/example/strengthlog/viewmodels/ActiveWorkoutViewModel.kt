package com.example.strengthlog.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.strengthlog.StrengthLogApplication
import com.example.strengthlog.data.local.entity.ExerciseEntity
import com.example.strengthlog.data.local.entity.WorkoutEntity
import com.example.strengthlog.data.local.entity.WorkoutExerciseEntity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class ExerciseWithSets(
    val exercise: ExerciseEntity,
    val sets: List<WorkoutExerciseEntity>
)

class ActiveWorkoutViewModel(
    application: Application,
    savedStateHandle: SavedStateHandle
) : AndroidViewModel(application) {

    private val workoutRepository =
        (application as StrengthLogApplication).workoutRepository

    private val exerciseRepository =
        (application as StrengthLogApplication).exerciseRepository

    private var currentWorkoutId: Int = -1

    private var timerJob: Job? = null
    private var exercisesJob: Job? = null

    private val _workoutName = MutableStateFlow("New Workout")
    val workoutName: StateFlow<String> = _workoutName.asStateFlow()

    private val _exercisesWithSets = MutableStateFlow<List<ExerciseWithSets>>(emptyList())
    val exercisesWithSets: StateFlow<List<ExerciseWithSets>> = _exercisesWithSets.asStateFlow()

    private val _elapsedSeconds = MutableStateFlow(0L)
    val elapsedSeconds: StateFlow<Long> = _elapsedSeconds.asStateFlow()

    private val _isFinished = MutableStateFlow(false)
    val isFinished: StateFlow<Boolean> = _isFinished.asStateFlow()

    init {
        val workoutIdArg = savedStateHandle.get<Int>("workoutId") ?: -1
        if (workoutIdArg != -1) {
            currentWorkoutId = workoutIdArg
            loadExistingWorkout(workoutIdArg)
        } else {
            createWorkout()
        }
        startTimer()
    }

    private fun loadExistingWorkout(workoutId: Int) {
        viewModelScope.launch {
            val workout = workoutRepository.getWorkoutById(workoutId) ?: return@launch
            _workoutName.value = workout.name
            _elapsedSeconds.value = (System.currentTimeMillis() - workout.timeStart) / 1000
            observeWorkoutExercises(workoutId)
        }
    }

    private fun observeWorkoutExercises(workoutId: Int) {
        exercisesJob?.cancel()
        exercisesJob = viewModelScope.launch {
            workoutRepository.getExercisesForWorkout(workoutId).collect { workoutExercises ->
                val exercisesWithSetsList = workoutExercises
                    .groupBy { it.exerciseId }
                    .mapNotNull { (exerciseId, sets) ->
                        val exercise = exerciseRepository.getExerciseById(exerciseId) ?: return@mapNotNull null
                        ExerciseWithSets(
                            exercise = exercise,
                            sets = sets.sortedBy { it.setNumber }
                        )
                    }

                // Merge with in-memory exercises that have empty sets
                val currentInMemory = _exercisesWithSets.value
                val mergedList = exercisesWithSetsList.toMutableList()
                currentInMemory.forEach { inMemory ->
                    if (inMemory.sets.isEmpty() && mergedList.none { it.exercise.exerciseId == inMemory.exercise.exerciseId }) {
                        mergedList.add(inMemory)
                    }
                }
                _exercisesWithSets.value = mergedList
            }
        }
    }

    private fun createWorkout() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        viewModelScope.launch {
            val workout = WorkoutEntity(
                userId = userId,
                name = _workoutName.value,
                note = null,
                timeStart = System.currentTimeMillis(),
                timeEnd = null
            )
            currentWorkoutId = workoutRepository.insertWorkout(workout).toInt()
            observeWorkoutExercises(currentWorkoutId)
        }
    }

    private fun startTimer() {
        timerJob = viewModelScope.launch {
            while (true) {
                delay(1000)
                _elapsedSeconds.value++
            }
        }
    }

    fun updateWorkoutName(name: String) {
        _workoutName.value = name
        if (currentWorkoutId == -1) return
        viewModelScope.launch {
            val workout = workoutRepository.getWorkoutById(currentWorkoutId) ?: return@launch
            workoutRepository.updateWorkout(workout.copy(name = name))
        }
    }

    fun addExercise(exercise: ExerciseEntity) {
        val alreadyAdded = _exercisesWithSets.value
            .any { it.exercise.exerciseId == exercise.exerciseId }
        if (alreadyAdded) return

        _exercisesWithSets.value = _exercisesWithSets.value +
                ExerciseWithSets(exercise = exercise, sets = emptyList())
    }

    fun addSet(exercise: ExerciseEntity) {
        if (currentWorkoutId == -1) return
        viewModelScope.launch {
            val currentSets = _exercisesWithSets.value
                .find { it.exercise.exerciseId == exercise.exerciseId }
                ?.sets ?: emptyList()

            val newSet = WorkoutExerciseEntity(
                workoutId = currentWorkoutId,
                exerciseId = exercise.exerciseId,
                setNumber = currentSets.size + 1,
                reps = 0,
                weight = 0f
            )
            workoutRepository.insertWorkoutExercise(newSet)
        }
    }

    fun updateSet(updatedSet: WorkoutExerciseEntity) {
        viewModelScope.launch {
            workoutRepository.updateWorkoutExercise(updatedSet)
        }
    }

    fun finishWorkout() {
        if (currentWorkoutId == -1) return
        viewModelScope.launch {
            val workout = workoutRepository.getWorkoutById(currentWorkoutId) ?: return@launch
            workoutRepository.updateWorkout(
                workout.copy(
                    name = _workoutName.value,
                    timeEnd = System.currentTimeMillis()
                )
            )
            timerJob?.cancel()
            _isFinished.value = true
        }
    }

    fun addExerciseById(exerciseId: String) {
        viewModelScope.launch {
            val exercise = exerciseRepository.getExerciseById(exerciseId) ?: return@launch
            addExercise(exercise)
        }
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
        exercisesJob?.cancel()
    }
}