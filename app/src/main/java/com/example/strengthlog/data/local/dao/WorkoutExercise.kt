package com.example.strengthlog.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.strengthlog.data.local.entity.WorkoutExerciseEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutExerciseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkoutExercise(workoutExercise: WorkoutExerciseEntity): Long

    @Query("SELECT * FROM workout_exercises WHERE workout_id = :workoutId")
    fun getExercisesForWorkout(workoutId: Int): Flow<List<WorkoutExerciseEntity>>

    @Update
    suspend fun updateWorkoutExercise(workoutExercise: WorkoutExerciseEntity): Int

    @Delete
    suspend fun deleteWorkoutExercise(workoutExercise: WorkoutExerciseEntity)
}