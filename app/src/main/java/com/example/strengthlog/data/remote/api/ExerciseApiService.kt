package com.example.strengthlog.data.remote.api

import com.example.strengthlog.data.remote.model.ExerciseApiResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ExerciseApiService {

    @GET("api/v1/exercises")
    suspend fun getExercises(
        @Query("limit") limit: Int = 50,
        @Query("after") after: String? = null
    ): ExerciseApiResponse
}