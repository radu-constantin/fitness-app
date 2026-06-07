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

    @Query("""
    SELECT DISTINCT e.name 
    FROM workout_exercises we 
    INNER JOIN exercises e ON we.exercise_id = e.exerciseId 
    WHERE we.workout_id = :workoutId 
    LIMIT 2
""")
    suspend fun getFirstTwoExerciseNames(workoutId: Int): List<String>

    @Query("DELETE FROM workout_exercises WHERE workout_id = :workoutId")
    suspend fun deleteExercisesForWorkout(workoutId: Int)
}