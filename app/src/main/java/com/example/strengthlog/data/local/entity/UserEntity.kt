package com.example.strengthlog.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity (
    @PrimaryKey val id: String,
    @ColumnInfo(name = "email") val email: String,
    @ColumnInfo(name= "display_name") val displayName: String,
    @ColumnInfo(name= "created_at") val createdAt: Long
)