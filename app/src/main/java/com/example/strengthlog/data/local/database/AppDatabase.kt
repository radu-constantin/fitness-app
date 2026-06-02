package com.example.strengthlog.data.local.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import android.content.Context
import com.example.strengthlog.data.local.dao.ExerciseDao
import com.example.strengthlog.data.local.dao.UserDao
import com.example.strengthlog.data.local.dao.WorkoutDao
import com.example.strengthlog.data.local.dao.WorkoutExerciseDao
import com.example.strengthlog.data.local.entity.ExerciseEntity
import com.example.strengthlog.data.local.entity.UserEntity
import com.example.strengthlog.data.local.entity.WorkoutEntity
import com.example.strengthlog.data.local.entity.WorkoutExerciseEntity

@Database(
    entities = [UserEntity::class, ExerciseEntity::class, WorkoutEntity::class, WorkoutExerciseEntity::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun exerciseDao(): ExerciseDao
    abstract fun workoutDao(): WorkoutDao
    abstract fun workoutExerciseDao(): WorkoutExerciseDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "strengthlog_database"
                ).build().also { INSTANCE = it }
            }
        }
    }
}