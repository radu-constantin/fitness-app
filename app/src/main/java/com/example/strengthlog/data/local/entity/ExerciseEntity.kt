package com.example.strengthlog.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exercises")
data class ExerciseEntity(
    @PrimaryKey val exerciseId: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "body_parts") val bodyParts: List<String>,
    @ColumnInfo(name = "equipments") val equipments: List<String>,
    @ColumnInfo(name = "target_muscles") val targetMuscles: List<String>
)