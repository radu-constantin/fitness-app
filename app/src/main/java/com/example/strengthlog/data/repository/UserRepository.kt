package com.example.strengthlog.data.repository

import com.example.strengthlog.data.local.dao.UserDao
import com.example.strengthlog.data.local.entity.UserEntity

class UserRepository(
    private val userDao: UserDao
) {

    suspend fun insertUser(user: UserEntity) {
        userDao.insertUser(user)
    }

    suspend fun getUserById(userId: String): UserEntity? {
        return userDao.getUserById(userId)
    }
}