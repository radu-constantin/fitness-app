package com.example.strengthlog.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.strengthlog.data.local.entity.WorkoutExerciseEntity

@Dao
interface WorkoutExerciseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkoutExercise(workoutExercise: WorkoutExerciseEntity)

    @Query("SELECT * FROM workout_exercises WHERE workout_id = :workoutId")
    suspend fun getExercisesForWorkout(workoutId: Int): List<WorkoutExerciseEntity>

    @Update
    suspend fun updateWorkoutExercise(workoutExercise: WorkoutExerciseEntity): Int

    @Delete
    suspend fun deleteWorkoutExercise(workoutExercise: WorkoutExerciseEntity)
}