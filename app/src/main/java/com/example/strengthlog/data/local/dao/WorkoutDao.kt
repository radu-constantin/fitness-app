    package com.example.strengthlog.data.local.dao

    import androidx.room.Dao
    import androidx.room.Delete
    import androidx.room.Insert
    import androidx.room.OnConflictStrategy
    import androidx.room.Query
    import androidx.room.Update
    import com.example.strengthlog.data.local.entity.WorkoutEntity
    import kotlinx.coroutines.flow.Flow

    @Dao
    interface WorkoutDao {
        @Insert(onConflict = OnConflictStrategy.REPLACE)
        suspend fun insertWorkout(workout: WorkoutEntity): Long

        @Query("SELECT * FROM workouts WHERE user_id = :userId ORDER BY time_start DESC")
        fun getWorkoutsByUser(userId: String): Flow<List<WorkoutEntity>>

        @Query("SELECT * FROM workouts WHERE id = :workoutId")
        suspend fun getWorkoutById(workoutId: Int): WorkoutEntity?

        @Update
        suspend fun updateWorkout(workout: WorkoutEntity): Int

        @Delete
        suspend fun deleteWorkout(workout: WorkoutEntity)
    }