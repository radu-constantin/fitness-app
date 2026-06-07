package com.example.strengthlog.data.repository

import com.example.strengthlog.data.local.dao.WorkoutDao
import com.example.strengthlog.data.local.dao.WorkoutExerciseDao
import com.example.strengthlog.data.local.entity.WorkoutEntity
import com.example.strengthlog.data.local.entity.WorkoutExerciseEntity
import kotlinx.coroutines.flow.Flow

class WorkoutRepository(
    private val workoutDao: WorkoutDao,
    private val workoutExerciseDao: WorkoutExerciseDao
) {

    suspend fun insertWorkout(workout: WorkoutEntity): Long {
        return workoutDao.insertWorkout(workout)
    }

    suspend fun updateWorkout(workout: WorkoutEntity) {
        workoutDao.updateWorkout(workout)
    }

    suspend fun deleteWorkout(workout: WorkoutEntity) {
        workoutExerciseDao.deleteExercisesForWorkout(workout.id)
        workoutDao.deleteWorkout(workout)
    }

    suspend fun getFirstTwoExerciseNames(workoutId: Int): List<String> {
        return workoutExerciseDao.getFirstTwoExerciseNames(workoutId)
    }

    suspend fun getWorkoutById(workoutId: Int): WorkoutEntity? {
        return workoutDao.getWorkoutById(workoutId)
    }

    fun getWorkoutsForUser(userId: String): Flow<List<WorkoutEntity>> {
        return workoutDao.getWorkoutsByUser(userId)
    }

    suspend fun insertWorkoutExercise(workoutExercise: WorkoutExerciseEntity): Long {
        return workoutExerciseDao.insertWorkoutExercise(workoutExercise)
    }

    suspend fun updateWorkoutExercise(workoutExercise: WorkoutExerciseEntity) {
        workoutExerciseDao.updateWorkoutExercise(workoutExercise)
    }

    suspend fun deleteWorkoutExercise(workoutExercise: WorkoutExerciseEntity) {
        workoutExerciseDao.deleteWorkoutExercise(workoutExercise)
    }

    fun getExercisesForWorkout(workoutId: Int): Flow<List<WorkoutExerciseEntity>> {
        return workoutExerciseDao.getExercisesForWorkout(workoutId)
    }
}