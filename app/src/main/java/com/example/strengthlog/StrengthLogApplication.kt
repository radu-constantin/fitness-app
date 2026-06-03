package com.example.strengthlog

import android.app.Application
import com.example.strengthlog.data.local.database.AppDatabase
import com.example.strengthlog.data.repository.ExerciseRepository
import com.example.strengthlog.data.repository.UserRepository
import com.example.strengthlog.data.repository.WorkoutRepository

class StrengthLogApplication : Application() {

    val workoutRepository: WorkoutRepository by lazy {
        val db = AppDatabase.getInstance(this)
        WorkoutRepository(db.workoutDao(), db.workoutExerciseDao())
    }

    val exerciseRepository: ExerciseRepository by lazy {
        val db = AppDatabase.getInstance(this)
        ExerciseRepository(db.exerciseDao())
    }

    val userRepository: UserRepository by lazy {
        val db = AppDatabase.getInstance(this)
        UserRepository(db.userDao())
    }
}