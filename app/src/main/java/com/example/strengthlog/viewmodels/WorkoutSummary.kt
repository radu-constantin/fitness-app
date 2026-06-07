package com.example.strengthlog.viewmodels

import com.example.strengthlog.data.local.entity.WorkoutEntity

data class WorkoutSummary(
    val workout: WorkoutEntity,
    val previewExercises: List<String>
)