package com.example.strengthlog.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.strengthlog.data.local.database.AppDatabase
import com.example.strengthlog.data.local.entity.WorkoutEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private val workoutDao = AppDatabase.getInstance(application).workoutDao()

    private val _workouts = MutableStateFlow<List<WorkoutEntity>>(emptyList())
    val workouts: StateFlow<List<WorkoutEntity>> = _workouts

    fun loadWorkouts(userId: String) {
        viewModelScope.launch {
            _workouts.value = workoutDao.getWorkoutsByUser(userId)
        }
    }
}