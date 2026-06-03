package com.example.strengthlog.data.repository

import com.example.strengthlog.data.local.dao.ExerciseDao
import com.example.strengthlog.data.local.entity.ExerciseEntity
import com.example.strengthlog.data.remote.api.ExerciseApiService
import kotlinx.coroutines.flow.Flow

class ExerciseRepository(
    private val exerciseDao: ExerciseDao,
    private val apiService: ExerciseApiService
) {

    fun getAllExercises(): Flow<List<ExerciseEntity>> {
        return exerciseDao.getAllExercises()
    }

    suspend fun fetchExercisesIfNeeded() {
        if (exerciseDao.getExerciseCount() > 0) return

        val response = apiService.getExercises(limit = 100, after = null)
        val entities = response.data.map { dto ->
            ExerciseEntity(
                exerciseId = dto.exerciseId,
                name = dto.name,
                bodyParts = dto.bodyParts,
                equipments = dto.equipments,
                targetMuscles = dto.targetMuscles
            )
        }
        exerciseDao.insertExercises(entities)
    }

    suspend fun getExerciseById(exerciseId: String): ExerciseEntity? {
        return exerciseDao.getExerciseById(exerciseId)
    }
}