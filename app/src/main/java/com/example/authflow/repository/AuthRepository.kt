package com.example.authflow.repository

import com.example.authflow.models.UserEntity
import com.example.authflow.network.UserDao

class AuthRepository(private val userDao: UserDao) {

    suspend fun register(user: UserEntity): Result<Unit> {
        return try {
            userDao.registerUser(user)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun login(email: String, password: String): Result<UserEntity> {
        return try {
            val user = userDao.getUserByEmail(email)
            if (user != null && user.password == password) {
                Result.success(user)
            } else {
                Result.failure(Exception("Invalid email or password"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
