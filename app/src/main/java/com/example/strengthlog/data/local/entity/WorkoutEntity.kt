package com.example.strengthlog.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName="workouts")
data class WorkoutEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name="user_id") val userId: String,
    @ColumnInfo(name="name") val name: String,
    @ColumnInfo(name="note") val note: String?,
    @ColumnInfo(name="time_start") val timeStart: Long,
    @ColumnInfo(name="time_end") val timeEnd: Long?
)