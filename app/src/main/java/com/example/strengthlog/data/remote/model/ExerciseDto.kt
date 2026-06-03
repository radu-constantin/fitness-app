package com.example.strengthlog.data.remote.model

data class ExerciseDto(
    val exerciseId: String,
    val name: String,
    val gifUrl: String,
    val bodyParts: List<String>,
    val equipments: List<String>,
    val targetMuscles: List<String>,
    val secondaryMuscles: List<String>,
    val instructions: List<String>
)

data class PaginationMeta(
    val total: Int,
    val hasNextPage: Boolean,
    val hasPreviousPage: Boolean,
    val nextCursor: String?,
    val previousCursor: String?
)

data class ExerciseApiResponse(
    val success: Boolean,
    val meta: PaginationMeta,
    val data: List<ExerciseDto>
)